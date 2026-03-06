package com.oceanview.dao.impl;

import com.oceanview.config.DBConnection;
import com.oceanview.dao.UserRoleDAO;
import com.oceanview.dto.UserRoleDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRoleDAOImpl implements UserRoleDAO {

    @Override
    public List<UserRoleDTO> findAll() {
        String sql = "SELECT role_id, role_name FROM user_roles ORDER BY role_name";
        List<UserRoleDTO> list = new ArrayList<>();

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                UserRoleDTO r = new UserRoleDTO();
                r.setRoleId(rs.getInt("role_id"));
                r.setRoleName(rs.getString("role_name"));
                list.add(r);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Load roles failed", e);
        }
        return list;
    }

    @Override
    public UserRoleDTO findById(int roleId) {
        String sql = "SELECT role_id, role_name FROM user_roles WHERE role_id = ?";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, roleId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                UserRoleDTO r = new UserRoleDTO();
                r.setRoleId(rs.getInt("role_id"));
                r.setRoleName(rs.getString("role_name"));
                return r;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Find role failed", e);
        }
    }
}