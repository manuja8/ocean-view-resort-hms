package com.oceanview.dao.impl;

import com.oceanview.config.DBConnection;
import com.oceanview.dao.BillDAO;
import com.oceanview.entity.Bill;
import com.oceanview.entity.Reservation;

import java.sql.*;

public class BillDAOImpl implements BillDAO {

    @Override
    public int save(Bill bill) {

        String sql =
                "INSERT INTO bills (reservation_id, total_amount, discount, tax, num_nights, created_by_user_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, bill.getReservation().getReservationNo());
            ps.setDouble(2, bill.getTotalAmount());
            ps.setDouble(3, bill.getDiscount());
            ps.setDouble(4, bill.getTax());
            ps.setInt(5, bill.getNumNights());
            ps.setInt(6, bill.getCreatedByUserId());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Saving bill failed", e);
        }
    }

    @Override
    public Bill findByReservation(int reservationNo) {

        // return latest active bill for reservation
        String sql =
                "SELECT bill_id, reservation_id, total_amount, discount, tax, num_nights, bill_date, is_canceled " +
                        "FROM bills WHERE reservation_id = ? AND is_canceled = 0 ORDER BY bill_id DESC LIMIT 1";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, reservationNo);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                Bill b = new Bill();
                b.setBillNo(rs.getInt("bill_id"));
                b.setTotalAmount(rs.getDouble("total_amount"));
                b.setDiscount(rs.getDouble("discount"));
                b.setTax(rs.getDouble("tax"));
                b.setNumNights(rs.getInt("num_nights"));
                b.setBillDate(rs.getTimestamp("bill_date").toLocalDateTime());
                b.setCanceled(rs.getBoolean("is_canceled"));

                Reservation r = new Reservation();
                r.setReservationNo(rs.getInt("reservation_id"));
                b.setReservation(r);

                return b;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Find bill by reservation failed", e);
        }
    }

    @Override
    public Bill findById(int billId) {

        String sql =
                "SELECT bill_id, reservation_id, total_amount, discount, tax, num_nights, bill_date, is_canceled " +
                        "FROM bills WHERE bill_id = ?";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, billId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                Bill b = new Bill();
                b.setBillNo(rs.getInt("bill_id"));
                b.setTotalAmount(rs.getDouble("total_amount"));
                b.setDiscount(rs.getDouble("discount"));
                b.setTax(rs.getDouble("tax"));
                b.setNumNights(rs.getInt("num_nights"));
                b.setBillDate(rs.getTimestamp("bill_date").toLocalDateTime());
                b.setCanceled(rs.getBoolean("is_canceled"));

                Reservation r = new Reservation();
                r.setReservationNo(rs.getInt("reservation_id"));
                b.setReservation(r);

                return b;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Find bill by id failed", e);
        }
    }

    @Override
    public void cancel(int billId, int updatedByUserId) {

        String sql =
                "UPDATE bills SET is_canceled = 1, updated_by_user_id = ?, updated_at = NOW() WHERE bill_id = ?";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, updatedByUserId);
            ps.setInt(2, billId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Cancel bill failed", e);
        }
    }
}