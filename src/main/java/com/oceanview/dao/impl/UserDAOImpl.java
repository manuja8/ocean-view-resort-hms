package com.oceanview.dao.impl;

import com.oceanview.config.DBConnection;
import com.oceanview.dao.UserDAO;
import com.oceanview.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAOImpl implements UserDAO {

    @Override
    public User login(String username) {

        User user = null;

        try {
            Connection con = DBConnection.getInstance().getConnection();

            String sql = "SELECT u.user_id, u.username, u.password_hash, u.is_active, u.is_blocked, r.role_name " +
                    "FROM users u JOIN user_roles r ON u.role_id = r.role_id " +
                    "WHERE u.username=? LIMIT 1";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setPasswordHash(rs.getString("password_hash"));
                user.setRoleName(rs.getString("role_name"));
                user.setActive(rs.getBoolean("is_active"));
                user.setBlocked(rs.getBoolean("is_blocked"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }
}
