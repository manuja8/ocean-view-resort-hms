package com.oceanview.dao.impl;

import com.oceanview.config.DBConnection;
import com.oceanview.dao.ReportDAO;
import com.oceanview.dto.ReportRowDTO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportDAOImpl implements ReportDAO {

    private Timestamp startTs(LocalDate from) {
        return (from == null) ? null : Timestamp.valueOf(from.atStartOfDay());
    }

    // end is exclusive (to + 1 day at 00:00)
    private Timestamp endTs(LocalDate to) {
        return (to == null) ? null : Timestamp.valueOf(to.plusDays(1).atStartOfDay());
    }

    private int queryInt(String sql, List<Object> params) {
        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Report query failed: " + e.getMessage(), e);
        }
    }

    private double queryDouble(String sql, List<Object> params) {
        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getDouble(1) : 0.0;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Report query failed: " + e.getMessage(), e);
        }
    }

    // common date filter builder for DATETIME columns
    private void appendDateRange(StringBuilder sql, List<Object> params, String column, LocalDate from, LocalDate to) {
        Timestamp s = startTs(from);
        Timestamp e = endTs(to);
        if (s != null) {
            sql.append(" AND ").append(column).append(" >= ? ");
            params.add(s);
        }
        if (e != null) {
            sql.append(" AND ").append(column).append(" < ? ");
            params.add(e);
        }
    }

    // ------------------- OCCUPANCY -------------------

    @Override
    public int countRooms() {
        String sql = "SELECT COUNT(*) FROM rooms";
        return queryInt(sql, new ArrayList<>());
    }

    @Override
    public int countRoomsByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM rooms WHERE LOWER(status) = ?";
        List<Object> params = new ArrayList<>();
        params.add(status.toLowerCase());
        return queryInt(sql, params);
    }

    @Override
    public int countReservations(LocalDate from, LocalDate to) {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM reservations WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();

        // Use COALESCE(check_in_date, created_at) so even booked reservations with null check_in_date are counted.
        appendDateRange(sql, params, "COALESCE(check_in_date, created_at)", from, to);

        return queryInt(sql.toString(), params);
    }

    @Override
    public int countReservationsByStatus(String status, LocalDate from, LocalDate to) {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM reservations WHERE LOWER(status) = ?"
        );
        List<Object> params = new ArrayList<>();
        params.add(status.toLowerCase());

        appendDateRange(sql, params, "COALESCE(check_in_date, created_at)", from, to);

        return queryInt(sql.toString(), params);
    }

    // ------------------- BILLS / REVENUE -------------------

    @Override
    public int countBills(LocalDate from, LocalDate to) {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM bills WHERE is_canceled = 0"
        );
        List<Object> params = new ArrayList<>();
        appendDateRange(sql, params, "bill_date", from, to);
        return queryInt(sql.toString(), params);
    }

    /**
     * "Paid Bill" = bill has at least one COMPLETED payment.
     */
    @Override
    public int countPaidBills(LocalDate from, LocalDate to) {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(DISTINCT b.bill_id) " +
                        "FROM bills b " +
                        "WHERE b.is_canceled = 0 " +
                        "AND EXISTS ( " +
                        "   SELECT 1 " +
                        "   FROM payments p " +
                        "   JOIN payment_statuses ps ON ps.payment_status_id = p.payment_status_id " +
                        "   WHERE p.bill_id = b.bill_id " +
                        "   AND LOWER(ps.status_name) = 'completed' " +
                        ")"
        );
        List<Object> params = new ArrayList<>();
        appendDateRange(sql, params, "b.bill_date", from, to);
        return queryInt(sql.toString(), params);
    }

    /**
     * "Unpaid Bill" = bill has NO completed payments (pending/failed payments still count as unpaid).
     */
    @Override
    public int countUnpaidBills(LocalDate from, LocalDate to) {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) " +
                        "FROM bills b " +
                        "WHERE b.is_canceled = 0 " +
                        "AND NOT EXISTS ( " +
                        "   SELECT 1 " +
                        "   FROM payments p " +
                        "   JOIN payment_statuses ps ON ps.payment_status_id = p.payment_status_id " +
                        "   WHERE p.bill_id = b.bill_id " +
                        "   AND LOWER(ps.status_name) = 'completed' " +
                        ")"
        );
        List<Object> params = new ArrayList<>();
        appendDateRange(sql, params, "b.bill_date", from, to);
        return queryInt(sql.toString(), params);
    }

    /**
     * Paid revenue = SUM(total_amount) of bills that have at least one COMPLETED payment.
     */
    @Override
    public double sumPaidRevenue(LocalDate from, LocalDate to) {
        StringBuilder sql = new StringBuilder(
                "SELECT COALESCE(SUM(b.total_amount), 0) " +
                        "FROM bills b " +
                        "WHERE b.is_canceled = 0 " +
                        "AND EXISTS ( " +
                        "   SELECT 1 " +
                        "   FROM payments p " +
                        "   JOIN payment_statuses ps ON ps.payment_status_id = p.payment_status_id " +
                        "   WHERE p.bill_id = b.bill_id " +
                        "   AND LOWER(ps.status_name) = 'completed' " +
                        ")"
        );
        List<Object> params = new ArrayList<>();
        appendDateRange(sql, params, "b.bill_date", from, to);
        return queryDouble(sql.toString(), params);
    }

    // ------------------- PAYMENTS -------------------

    /**
     * Total payment records in range (all statuses).
     */
    @Override
    public int countPayments(LocalDate from, LocalDate to) {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(*) FROM payments WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();
        appendDateRange(sql, params, "payment_date", from, to);
        return queryInt(sql.toString(), params);
    }

    /**
     * Total amount received = SUM of COMPLETED payments only.
     */
    @Override
    public double sumPayments(LocalDate from, LocalDate to) {
        StringBuilder sql = new StringBuilder(
                "SELECT COALESCE(SUM(p.amount), 0) " +
                        "FROM payments p " +
                        "JOIN payment_statuses ps ON ps.payment_status_id = p.payment_status_id " +
                        "WHERE LOWER(ps.status_name) = 'completed'"
        );
        List<Object> params = new ArrayList<>();
        appendDateRange(sql, params, "p.payment_date", from, to);
        return queryDouble(sql.toString(), params);
    }

    @Override
    public List<ReportRowDTO> paymentStatusBreakdown(LocalDate from, LocalDate to) {

        StringBuilder sql = new StringBuilder(
                "SELECT ps.status_name, COUNT(*) AS cnt " +
                        "FROM payments p " +
                        "JOIN payment_statuses ps ON ps.payment_status_id = p.payment_status_id " +
                        "WHERE 1=1"
        );
        List<Object> params = new ArrayList<>();
        appendDateRange(sql, params, "p.payment_date", from, to);
        sql.append(" GROUP BY ps.status_name ORDER BY cnt DESC");

        List<ReportRowDTO> rows = new ArrayList<>();

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new ReportRowDTO(
                            "Payments - " + rs.getString("status_name"),
                            String.valueOf(rs.getInt("cnt"))
                    ));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Payment status breakdown failed", e);
        }

        return rows;
    }
}