package com.oceanview.dao.impl;

import com.oceanview.config.DBConnection;
import com.oceanview.dao.ComplaintDAO;
import com.oceanview.entity.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComplaintDAOImpl implements ComplaintDAO {

    // Use singleton DBConnection instance
    private Connection con = DBConnection.getInstance().getConnection();

    @Override
    public void save(Complaint complaint) {

        String sql = "INSERT INTO complaints (guest_id, reservation_id, subject, description, status, priority, created_by_user_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, complaint.getGuest().getGuestId());

            if (complaint.getReservation() != null)
                ps.setInt(2, complaint.getReservation().getReservationNo());
            else
                ps.setNull(2, Types.INTEGER);

            ps.setString(3, complaint.getSubject());
            ps.setString(4, complaint.getDescription());
            ps.setString(5, complaint.getStatus());
            ps.setString(6, complaint.getPriority());
            ps.setInt(7, 1); // temporary user

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateStatus(int complaintId, String status) {

        String sql = "UPDATE complaints SET status=? WHERE complaint_id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, complaintId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Complaint> findAll() {

        List<Complaint> list = new ArrayList<>();
        String sql = "SELECT * FROM complaints";

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Complaint c = new Complaint();
                c.setComplaintId(rs.getInt("complaint_id"));
                c.setSubject(rs.getString("subject"));
                c.setDescription(rs.getString("description"));
                c.setStatus(rs.getString("status"));
                c.setPriority(rs.getString("priority"));

                list.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<Complaint> findByStatus(String status) {

        List<Complaint> list = new ArrayList<>();
        String sql = "SELECT * FROM complaints WHERE status=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Complaint c = new Complaint();
                c.setComplaintId(rs.getInt("complaint_id"));
                c.setSubject(rs.getString("subject"));
                c.setDescription(rs.getString("description"));
                c.setStatus(rs.getString("status"));
                c.setPriority(rs.getString("priority"));

                list.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}