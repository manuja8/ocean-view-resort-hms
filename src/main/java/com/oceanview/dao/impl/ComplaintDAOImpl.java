package com.oceanview.dao.impl;

import com.oceanview.config.DBConnection;
import com.oceanview.dao.ComplaintDAO;
import com.oceanview.dto.ComplaintDTO;
import com.oceanview.entity.Complaint;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComplaintDAOImpl implements ComplaintDAO {

    @Override
    public int save(Complaint c) {

        String sql = "INSERT INTO complaints " +
                "(guest_id, reservation_id, subject, description, status, priority, created_by_user_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, c.getGuestId());

            if (c.getReservationId() == null) ps.setNull(2, Types.INTEGER);
            else ps.setInt(2, c.getReservationId());

            ps.setString(3, c.getSubject());
            ps.setString(4, c.getDescription());
            ps.setString(5, c.getStatus());
            ps.setString(6, c.getPriority());
            ps.setInt(7, c.getCreatedByUserId());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Saving complaint failed", e);
        }
    }

    @Override
    public boolean update(Complaint c) {

        String sql = "UPDATE complaints SET " +
                "guest_id=?, reservation_id=?, subject=?, description=?, status=?, priority=?, " +
                "updated_by_user_id=?, updated_at=NOW() " +
                "WHERE complaint_id=?";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, c.getGuestId());

            if (c.getReservationId() == null) ps.setNull(2, Types.INTEGER);
            else ps.setInt(2, c.getReservationId());

            ps.setString(3, c.getSubject());
            ps.setString(4, c.getDescription());
            ps.setString(5, c.getStatus());
            ps.setString(6, c.getPriority());

            if (c.getUpdatedByUserId() == null) ps.setNull(7, Types.INTEGER);
            else ps.setInt(7, c.getUpdatedByUserId());

            ps.setInt(8, c.getComplaintId());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new RuntimeException("Updating complaint failed", e);
        }
    }

    @Override
    public boolean delete(int complaintId) {
        String sql = "DELETE FROM complaints WHERE complaint_id=?";
        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, complaintId);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new RuntimeException("Delete complaint failed", e);
        }
    }

    @Override
    public ComplaintDTO findById(int complaintId) {

        String sql =
                "SELECT c.complaint_id, c.guest_id, c.reservation_id, c.subject, c.description, c.status, c.priority, c.created_at, " +
                        "g.full_name AS guest_name, g.contact_no AS guest_contact, " +
                        "r.reservation_number " +
                        "FROM complaints c " +
                        "JOIN guests g ON g.guest_id = c.guest_id " +
                        "LEFT JOIN reservations r ON r.reservation_id = c.reservation_id " +
                        "WHERE c.complaint_id = ?";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, complaintId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return mapDTO(rs);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Find complaint failed", e);
        }
    }

    @Override
    public List<ComplaintDTO> search(String q, String status, String priority) {

        StringBuilder sql = new StringBuilder(
                "SELECT c.complaint_id, c.guest_id, c.reservation_id, c.subject, c.description, c.status, c.priority, c.created_at, " +
                        "g.full_name AS guest_name, g.contact_no AS guest_contact, " +
                        "r.reservation_number " +
                        "FROM complaints c " +
                        "JOIN guests g ON g.guest_id = c.guest_id " +
                        "LEFT JOIN reservations r ON r.reservation_id = c.reservation_id " +
                        "WHERE 1=1 "
        );

        List<Object> params = new ArrayList<>();

        if (q != null && !q.trim().isEmpty()) {
            String like = "%" + q.trim() + "%";
            sql.append(" AND (c.subject LIKE ? OR c.description LIKE ? OR g.full_name LIKE ? OR g.contact_no LIKE ? OR r.reservation_number LIKE ?) ");
            params.add(like);
            params.add(like);
            params.add(like);
            params.add(like);
            params.add(like);
        }

        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND LOWER(c.status)=? ");
            params.add(status.trim().toLowerCase());
        }

        if (priority != null && !priority.trim().isEmpty()) {
            sql.append(" AND LOWER(c.priority)=? ");
            params.add(priority.trim().toLowerCase());
        }

        sql.append(" ORDER BY c.complaint_id DESC");

        List<ComplaintDTO> list = new ArrayList<>();

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapDTO(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Search complaints failed", e);
        }

        return list;
    }

    private ComplaintDTO mapDTO(ResultSet rs) throws SQLException {
        ComplaintDTO d = new ComplaintDTO();
        d.setComplaintId(rs.getInt("complaint_id"));
        d.setGuestId(rs.getInt("guest_id"));
        d.setReservationId((Integer) rs.getObject("reservation_id"));
        d.setSubject(rs.getString("subject"));
        d.setDescription(rs.getString("description"));
        d.setStatus(rs.getString("status"));
        d.setPriority(rs.getString("priority"));

        Timestamp ts = rs.getTimestamp("created_at");
        d.setCreatedAt(ts != null ? ts.toString() : null);

        d.setGuestName(rs.getString("guest_name"));
        d.setGuestContact(rs.getString("guest_contact"));
        d.setReservationNumber(rs.getString("reservation_number"));

        return d;
    }
}