package com.oceanview.dao.impl;

import com.oceanview.config.DBConnection;
import com.oceanview.dao.BillPrintLogDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class BillPrintLogDAOImpl implements BillPrintLogDAO {

    @Override
    public void logPrint(int billId, int printedByUserId) {

        String sql = "INSERT INTO bill_print_logs (bill_id, printed_by_user_id) VALUES (?, ?)";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, billId);
            ps.setInt(2, printedByUserId);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Print log failed", e);
        }
    }
}