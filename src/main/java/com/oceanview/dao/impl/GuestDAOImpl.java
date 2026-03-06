package com.oceanview.dao.impl;

import com.oceanview.config.DBConnection;
import com.oceanview.dao.GuestDAO;
import com.oceanview.entity.Guest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuestDAOImpl implements GuestDAO {

    @Override
    public int save(Guest g, int createdByUserId) {

        String sql = "INSERT INTO guests " +
                "(full_name, gender, date_of_birth, address, contact_no, email, identification_no, identification_type, created_by_user_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, g.getFullName());
            ps.setString(2, isBlank(g.getGender()) ? null : g.getGender());

            if (g.getDateOfBirth() == null) ps.setNull(3, Types.DATE);
            else ps.setDate(3, Date.valueOf(g.getDateOfBirth()));

            ps.setString(4, g.getAddress());
            ps.setString(5, g.getContactNo());
            ps.setString(6, isBlank(g.getEmail()) ? null : g.getEmail());
            ps.setString(7, g.getIdentificationNo());
            ps.setString(8, g.getIdentificationType());
            ps.setInt(9, createdByUserId);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Guest save failed (ID number may already exist).", e);
        }
    }

    @Override
    public boolean update(Guest g, int updatedByUserId) {

        String sql = "UPDATE guests SET " +
                "full_name=?, gender=?, date_of_birth=?, address=?, contact_no=?, email=?, identification_no=?, identification_type=?, " +
                "updated_by_user_id=?, updated_at=NOW() " +
                "WHERE guest_id=?";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, g.getFullName());
            ps.setString(2, isBlank(g.getGender()) ? null : g.getGender());

            if (g.getDateOfBirth() == null) ps.setNull(3, Types.DATE);
            else ps.setDate(3, Date.valueOf(g.getDateOfBirth()));

            ps.setString(4, g.getAddress());
            ps.setString(5, g.getContactNo());
            ps.setString(6, isBlank(g.getEmail()) ? null : g.getEmail());
            ps.setString(7, g.getIdentificationNo());
            ps.setString(8, g.getIdentificationType());
            ps.setInt(9, updatedByUserId);
            ps.setInt(10, g.getGuestId());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new RuntimeException("Guest update failed (ID number may already exist).", e);
        }
    }

    @Override
    public boolean delete(int guestId) {
        String sql = "DELETE FROM guests WHERE guest_id=?";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, guestId);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {

            throw new RuntimeException("Cannot delete guest (guest may have reservations).", e);
        }
    }

    @Override
    public Guest findById(int id) {
        String sql = "SELECT guest_id, full_name, gender, date_of_birth, address, contact_no, email, identification_no, identification_type " +
                "FROM guests WHERE guest_id=?";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Find guest failed", e);
        }
    }

    @Override
    public List<Guest> findAll() {
        String sql = "SELECT guest_id, full_name, gender, date_of_birth, address, contact_no, email, identification_no, identification_type " +
                "FROM guests ORDER BY guest_id DESC";

        List<Guest> list = new ArrayList<>();

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(map(rs));

        } catch (SQLException e) {
            throw new RuntimeException("List guests failed", e);
        }

        return list;
    }

    @Override
    public List<Guest> search(String q) {
        String kw = (q == null) ? "" : q.trim();
        String like = "%" + kw + "%";

        String sql = "SELECT guest_id, full_name, gender, date_of_birth, address, contact_no, email, identification_no, identification_type " +
                "FROM guests " +
                "WHERE full_name LIKE ? OR contact_no LIKE ? OR identification_no LIKE ? OR email LIKE ? " +
                "ORDER BY guest_id DESC";

        List<Guest> list = new ArrayList<>();

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            ps.setString(4, like);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Search guests failed", e);
        }

        return list;
    }

    private Guest map(ResultSet rs) throws SQLException {
        Guest g = new Guest();
        g.setGuestId(rs.getInt("guest_id"));
        g.setFullName(rs.getString("full_name"));
        g.setGender(rs.getString("gender"));

        Date dob = rs.getDate("date_of_birth");
        if (dob != null) g.setDateOfBirth(dob.toLocalDate());

        g.setAddress(rs.getString("address"));
        g.setContactNo(rs.getString("contact_no"));
        g.setEmail(rs.getString("email"));
        g.setIdentificationNo(rs.getString("identification_no"));
        g.setIdentificationType(rs.getString("identification_type"));
        return g;
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}