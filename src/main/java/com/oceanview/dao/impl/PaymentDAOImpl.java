package com.oceanview.dao.impl;

import com.oceanview.config.DBConnection;
import com.oceanview.dao.PaymentDAO;
import com.oceanview.entity.Bill;
import com.oceanview.entity.Payment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PaymentDAOImpl implements PaymentDAO {

    // Use singleton DBConnection instance
    private Connection con = DBConnection.getInstance().getConnection();

    @Override
    public void save(Payment payment) {

        String sql = "INSERT INTO payments (bill_id, amount, payment_method_id, payment_status_id, payment_reference, created_by_user_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, payment.getBill().getBillNo());
            ps.setDouble(2, payment.getAmount());
            ps.setInt(3, payment.getPaymentMethodId());
            ps.setInt(4, payment.getPaymentStatusId());
            ps.setString(5, payment.getPaymentReference());
            ps.setInt(6, 1); // temporary hardcoded user (replace with session later)

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean existsByBillId(int billId) {

        String sql = "SELECT payment_id FROM payments WHERE bill_id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, billId);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}