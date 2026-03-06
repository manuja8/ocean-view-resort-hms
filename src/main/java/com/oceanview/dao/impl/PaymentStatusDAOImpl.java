package com.oceanview.dao.impl;

import com.oceanview.config.DBConnection;
import com.oceanview.dao.PaymentStatusDAO;
import com.oceanview.dto.PaymentStatusDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentStatusDAOImpl implements PaymentStatusDAO {

    @Override
    public List<PaymentStatusDTO> findAll() {
        String sql = "SELECT payment_status_id, status_name FROM payment_statuses ORDER BY status_name";
        List<PaymentStatusDTO> list = new ArrayList<>();

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PaymentStatusDTO d = new PaymentStatusDTO();
                d.setId(rs.getInt("payment_status_id"));
                d.setName(rs.getString("status_name"));
                list.add(d);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Load payment statuses failed", e);
        }

        return list;
    }
}