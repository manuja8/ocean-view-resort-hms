package com.oceanview.dao.impl;

import com.oceanview.config.DBConnection;
import com.oceanview.dao.GuestDAO;
import com.oceanview.entity.Guest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GuestDAOImpl implements GuestDAO {

    private Connection con = DBConnection.getInstance().getConnection();

    @Override
    public int save(Guest guest) {
        try {
            String sql = "INSERT INTO guests (name, phone, email) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, guest.getName());
            ps.setString(2, guest.getContactNumber());
            ps.setString(3, guest.getEmail());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void update(Guest guest) {
        try {
            String sql = "UPDATE guests SET name=?, phone=?, email=? WHERE guest_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, guest.getName());
            ps.setString(2, guest.getContactNumber());
            ps.setString(3, guest.getEmail());
            ps.setInt(4, guest.getGuestId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean delete(int guestId) {
        return false;
    }

    @Override
    public Guest findById(int id) {
        try {
            String sql = "SELECT * FROM guests WHERE guest_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Guest g = new Guest();
                g.setGuestId(rs.getInt("guest_id"));
                g.setName(rs.getString("name"));
                g.setContactNumber(rs.getString("phone"));
                g.setEmail(rs.getString("email"));
                return g;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Guest> findAll() {
        List<Guest> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM guests";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Guest g = new Guest();
                g.setGuestId(rs.getInt("guest_id"));
                g.setName(rs.getString("name"));
                g.setContactNumber(rs.getString("phone"));
                g.setEmail(rs.getString("email"));
                list.add(g);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Guest> search(String query) {
        List<Guest> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM guests WHERE name LIKE ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + query + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Guest g = new Guest();
                g.setGuestId(rs.getInt("guest_id"));
                g.setName(rs.getString("name"));
                g.setContactNumber(rs.getString("phone"));
                g.setEmail(rs.getString("email"));
                list.add(g);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}