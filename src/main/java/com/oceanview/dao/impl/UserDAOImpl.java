package com.oceanview.dao.impl;

import com.oceanview.config.DBConnection;
import com.oceanview.dao.UserDAO;
import com.oceanview.entity.User;
import com.oceanview.factory.UserFactory;
import com.oceanview.entity.Admin;
import com.oceanview.entity.Receptionist;

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
                String role = rs.getString("role_name");

                user = UserFactory.createUser(role);

                if (user != null) {
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setPasswordHash(rs.getString("password_hash"));
                    user.setRoleName(role);
                    user.setActive(rs.getBoolean("is_active"));
                    user.setBlocked(rs.getBoolean("is_blocked"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;


    }

    @Override
    public int save(User user) {

        int generatedId = 0;

        try {
            Connection con = DBConnection.getInstance().getConnection();

            String sql = "INSERT INTO users (username, full_name, address, contact_no, role_id, password_hash, expiry_date) " +
                    "VALUES (?, ?, ?, ?, (SELECT role_id FROM user_roles WHERE role_name=?), ?, NOW() + INTERVAL 1 YEAR)";

            PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setString(1, user.getUsername());
            ps.setString(2, "Temp Name"); // adjust if needed
            ps.setString(3, "Temp Address");
            ps.setString(4, "0000000000");
            ps.setString(5, user.getRoleName());
            ps.setString(6, user.getPasswordHash());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                generatedId = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return generatedId;
    }

    @Override
    public void update(User user) {

    }

    @Override
    public void delete(int userId) {

    }

    @Override
    public User findById(int userId) {

        return null;
    }

    @Override
    public java.util.List<User> findAll() {

        return null;
    }

    @Override
    public java.util.List<User> search(String keyword) {

        return null;
    }
}

