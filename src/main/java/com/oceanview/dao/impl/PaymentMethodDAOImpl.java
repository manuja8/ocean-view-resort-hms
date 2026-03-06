package com.oceanview.dao.impl;

import com.oceanview.config.DBConnection;
import com.oceanview.dao.PaymentMethodDAO;
import com.oceanview.dto.PaymentMethodDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentMethodDAOImpl implements PaymentMethodDAO {

    @Override
    public List<PaymentMethodDTO> findAll() {
        String sql = "SELECT payment_method_id, method_name FROM payment_methods ORDER BY method_name";
        List<PaymentMethodDTO> list = new ArrayList<>();

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PaymentMethodDTO d = new PaymentMethodDTO();
                d.setId(rs.getInt("payment_method_id"));
                d.setName(rs.getString("method_name"));
                list.add(d);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Load payment methods failed", e);
        }

        return list;
    }
}