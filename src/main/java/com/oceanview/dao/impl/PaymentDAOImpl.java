package com.oceanview.dao.impl;

import com.oceanview.config.DBConnection;
import com.oceanview.dao.PaymentDAO;
import com.oceanview.dto.PaymentDTO;
import com.oceanview.entity.Payment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAOImpl implements PaymentDAO {

    @Override
    public int save(Payment p) {

        String sql = "INSERT INTO payments " +
                "(bill_id, amount, payment_method_id, payment_status_id, payment_reference, created_by_user_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, p.getBillId());
            ps.setDouble(2, p.getAmount());
            ps.setInt(3, p.getPaymentMethodId());
            ps.setInt(4, p.getPaymentStatusId());
            ps.setString(5, p.getPaymentReference());
            ps.setInt(6, p.getCreatedByUserId());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Save payment failed", e);
        }
    }

    @Override
    public List<PaymentDTO> findAll(String q, Integer statusId) {

        StringBuilder sql = new StringBuilder(
                "SELECT p.payment_id, p.bill_id, p.amount, p.payment_reference, p.payment_date, " +
                        "pm.payment_method_id, pm.method_name, " +
                        "ps.payment_status_id, ps.status_name " +
                        "FROM payments p " +
                        "JOIN payment_methods pm ON pm.payment_method_id = p.payment_method_id " +
                        "JOIN payment_statuses ps ON ps.payment_status_id = p.payment_status_id " +
                        "WHERE 1=1 "
        );

        List<Object> params = new ArrayList<>();

        if (q != null && !q.trim().isEmpty()) {
            sql.append(" AND (CAST(p.bill_id AS CHAR) LIKE ? OR p.payment_reference LIKE ?) ");
            String like = "%" + q.trim() + "%";
            params.add(like);
            params.add(like);
        }

        if (statusId != null && statusId > 0) {
            sql.append(" AND p.payment_status_id = ? ");
            params.add(statusId);
        }

        sql.append(" ORDER BY p.payment_id DESC");

        List<PaymentDTO> list = new ArrayList<>();

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PaymentDTO d = new PaymentDTO();
                    d.setPaymentId(rs.getInt("payment_id"));
                    d.setBillId(rs.getInt("bill_id"));
                    d.setAmount(rs.getDouble("amount"));
                    d.setPaymentReference(rs.getString("payment_reference"));
                    Timestamp ts = rs.getTimestamp("payment_date");
                    d.setPaymentDate(ts != null ? ts.toString() : null);

                    d.setPaymentMethodId(rs.getInt("payment_method_id"));
                    d.setPaymentMethodName(rs.getString("method_name"));

                    d.setPaymentStatusId(rs.getInt("payment_status_id"));
                    d.setPaymentStatusName(rs.getString("status_name"));

                    list.add(d);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Load payments failed", e);
        }

        return list;
    }

    @Override
    public double sumCompletedPaymentsForBill(int billId) {

        // Count only completed payments for "paid amount"
        String sql =
                "SELECT COALESCE(SUM(p.amount), 0) " +
                        "FROM payments p " +
                        "JOIN payment_statuses ps ON ps.payment_status_id = p.payment_status_id " +
                        "WHERE p.bill_id = ? AND LOWER(ps.status_name) = 'completed'";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, billId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getDouble(1) : 0.0;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Sum payments failed", e);
        }
    }
}