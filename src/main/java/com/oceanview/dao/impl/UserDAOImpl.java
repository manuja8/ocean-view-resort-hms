package com.oceanview.dao.impl;

import com.oceanview.config.DBConnection;
import com.oceanview.dao.UserDAO;
import com.oceanview.entity.User;
import com.oceanview.factory.UserFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    @Override
    public User login(String username) {

        String sql =
                "SELECT u.user_id, u.username, u.password_hash, u.full_name, u.address, u.contact_no, " +
                        "       u.role_id, r.role_name, u.is_active, u.is_blocked, " +
                        "       u.effective_date, u.expiry_date, u.last_login " +
                        "FROM users u " +
                        "JOIN user_roles r ON u.role_id = r.role_id " +
                        "WHERE u.username=? LIMIT 1";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                String role = rs.getString("role_name");
                User user = UserFactory.createUser(role);
                if (user == null) return null;

                mapUser(rs, user);
                return user;
            }

        } catch (SQLException e) {
            throw new RuntimeException("User login failed", e);
        }
    }

    @Override
    public void updateLastLogin(int userId) {
        String sql = "UPDATE users SET last_login = NOW() WHERE user_id = ?";
        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Update last_login failed", e);
        }
    }

    @Override
    public int save(User user) {

        String sql =
                "INSERT INTO users " +
                        "(username, full_name, address, contact_no, role_id, password_hash, is_active, is_blocked, expiry_date, created_by_user_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getFullName());
            ps.setString(3, user.getAddress());
            ps.setString(4, user.getContactNo());
            ps.setInt(5, user.getRoleId());
            ps.setString(6, user.getPasswordHash());
            ps.setBoolean(7, user.isActive());
            ps.setBoolean(8, user.isBlocked());
            ps.setTimestamp(9, Timestamp.valueOf(user.getExpiryDate()));

            if (user.getCreatedByUserId() == null) ps.setNull(10, Types.INTEGER);
            else ps.setInt(10, user.getCreatedByUserId());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException("User save failed (duplicate username/contact?)", e);
        }
    }

    @Override
    public void update(User user) {

        // If passwordHash is null -> keep old password_hash
        String sql =
                "UPDATE users SET " +
                        " username=?, full_name=?, address=?, contact_no=?, role_id=?, " +
                        " is_active=?, is_blocked=?, expiry_date=?, " +
                        " password_hash = COALESCE(?, password_hash), " +
                        " updated_by_user_id=?, updated_at=NOW() " +
                        "WHERE user_id=?";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getFullName());
            ps.setString(3, user.getAddress());
            ps.setString(4, user.getContactNo());
            ps.setInt(5, user.getRoleId());
            ps.setBoolean(6, user.isActive());
            ps.setBoolean(7, user.isBlocked());
            ps.setTimestamp(8, Timestamp.valueOf(user.getExpiryDate()));
            ps.setString(9, user.getPasswordHash()); // null -> keep existing

            if (user.getUpdatedByUserId() == null) ps.setNull(10, Types.INTEGER);
            else ps.setInt(10, user.getUpdatedByUserId());

            ps.setInt(11, user.getUserId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("User update failed", e);
        }
    }

    @Override
    public void delete(int userId) {
        String sql = "DELETE FROM users WHERE user_id=?";
        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("User delete failed (may be referenced by other records)", e);
        }
    }

    @Override
    public User findById(int userId) {

        String sql =
                "SELECT u.user_id, u.username, u.password_hash, u.full_name, u.address, u.contact_no, " +
                        "       u.role_id, r.role_name, u.is_active, u.is_blocked, " +
                        "       u.effective_date, u.expiry_date, u.last_login " +
                        "FROM users u " +
                        "JOIN user_roles r ON u.role_id = r.role_id " +
                        "WHERE u.user_id=?";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                String role = rs.getString("role_name");
                User user = UserFactory.createUser(role);
                if (user == null) return null;

                mapUser(rs, user);
                return user;
            }

        } catch (SQLException e) {
            throw new RuntimeException("User findById failed", e);
        }
    }

    @Override
    public List<User> findAll() {

        String sql =
                "SELECT u.user_id, u.username, u.password_hash, u.full_name, u.address, u.contact_no, " +
                        "       u.role_id, r.role_name, u.is_active, u.is_blocked, " +
                        "       u.effective_date, u.expiry_date, u.last_login " +
                        "FROM users u " +
                        "JOIN user_roles r ON u.role_id = r.role_id " +
                        "ORDER BY u.user_id DESC";

        List<User> list = new ArrayList<>();

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String role = rs.getString("role_name");
                User user = UserFactory.createUser(role);
                if (user == null) continue;
                mapUser(rs, user);
                list.add(user);
            }

        } catch (SQLException e) {
            throw new RuntimeException("User findAll failed", e);
        }

        return list;
    }

    @Override
    public List<User> search(String keyword) {

        String kw = (keyword == null) ? "" : keyword.trim();
        String like = "%" + kw + "%";

        String sql =
                "SELECT u.user_id, u.username, u.password_hash, u.full_name, u.address, u.contact_no, " +
                        "       u.role_id, r.role_name, u.is_active, u.is_blocked, " +
                        "       u.effective_date, u.expiry_date, u.last_login " +
                        "FROM users u " +
                        "JOIN user_roles r ON u.role_id = r.role_id " +
                        "WHERE u.username LIKE ? OR u.full_name LIKE ? OR u.contact_no LIKE ? OR r.role_name LIKE ? " +
                        "ORDER BY u.user_id DESC";

        List<User> list = new ArrayList<>();

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            ps.setString(4, like);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String role = rs.getString("role_name");
                    User user = UserFactory.createUser(role);
                    if (user == null) continue;
                    mapUser(rs, user);
                    list.add(user);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("User search failed", e);
        }

        return list;
    }

    private void mapUser(ResultSet rs, User user) throws SQLException {

        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPasswordHash(rs.getString("password_hash"));

        user.setFullName(rs.getString("full_name"));
        user.setAddress(rs.getString("address"));
        user.setContactNo(rs.getString("contact_no"));

        user.setRoleId(rs.getInt("role_id"));
        user.setRoleName(rs.getString("role_name"));

        user.setActive(rs.getBoolean("is_active"));
        user.setBlocked(rs.getBoolean("is_blocked"));

        Timestamp eff = rs.getTimestamp("effective_date");
        if (eff != null) user.setEffectiveDate(eff.toLocalDateTime());

        Timestamp exp = rs.getTimestamp("expiry_date");
        if (exp != null) user.setExpiryDate(exp.toLocalDateTime());

        Timestamp last = rs.getTimestamp("last_login");
        if (last != null) user.setLastLogin(last.toLocalDateTime());
    }
}



/*
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
        String sql = "SELECT u.user_id, u.username, u.password_hash, u.is_active, u.is_blocked, " +
                "u.effective_date, u.expiry_date, u.last_login, r.role_name " +
                "FROM users u " +
                "JOIN user_roles r ON u.role_id = r.role_id " +
                "WHERE u.username=? LIMIT 1";

        // Using try-with-resources to ensure proper resource management
        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String role = rs.getString("role_name");
                    user = UserFactory.createUser(role);

                    if (user == null) {
                        return null; // unknown role -> fail safely
                    }

                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setPasswordHash(rs.getString("password_hash"));
                    user.setRoleName(role);
                    user.setActive(rs.getBoolean("is_active"));
                    user.setBlocked(rs.getBoolean("is_blocked"));

                    // Map the effective and expiry date to LocalDateTime (if available)
                    var eff = rs.getTimestamp("effective_date");
                    var exp = rs.getTimestamp("expiry_date");
                    if (eff != null) user.setEffectiveDate(eff.toLocalDateTime());
                    if (exp != null) user.setExpiryDate(exp.toLocalDateTime());

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

        String sql = "INSERT INTO users (username, full_name, address, contact_no, role_id, password_hash, expiry_date) " +
                "VALUES (?, ?, ?, ?, (SELECT role_id FROM user_roles WHERE role_name=?), ?, NOW() + INTERVAL 1 YEAR)";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, "Temp Name");
            ps.setString(3, "Temp Address");
            ps.setString(4, "0000000000");
            ps.setString(5, user.getRoleName());
            ps.setString(6, user.getPasswordHash());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return generatedId;
    }

    @Override
    public void updateLastLogin(int userId) {
        String sql = "UPDATE users SET last_login = NOW() WHERE user_id = ?";
        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

*/
