package com.oceanview.dao.impl;

import com.oceanview.config.DBConnection;
import com.oceanview.dao.ReservationDAO;
import com.oceanview.entity.Guest;
import com.oceanview.entity.Reservation;
import com.oceanview.entity.Room;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAOImpl implements ReservationDAO {

    @Override
    public int save(Reservation r) {

        String sql = "INSERT INTO reservations " +
                "(reservation_number, guest_id, room_id, check_in_date, check_out_date, status, created_by_user_id, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, r.getReservationNumber());
            ps.setInt(2, r.getGuest().getGuestId());
            ps.setInt(3, r.getRoom().getRoomId());
            ps.setTimestamp(4, Timestamp.valueOf(r.getCheckIn().atStartOfDay()));
            ps.setTimestamp(5, Timestamp.valueOf(r.getCheckOut().atStartOfDay()));
            ps.setString(6, normalizeStatus(r.getStatus()));
            ps.setInt(7, r.getCreatedByUserId());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Saving reservation failed", e);
        }
    }

    @Override
    public boolean update(Reservation r) {

        String sql = "UPDATE reservations SET " +
                "guest_id=?, room_id=?, check_in_date=?, check_out_date=?, status=?, updated_by_user_id=?, updated_at=NOW() " +
                "WHERE reservation_id=?";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, r.getGuest().getGuestId());
            ps.setInt(2, r.getRoom().getRoomId());
            ps.setTimestamp(3, Timestamp.valueOf(r.getCheckIn().atStartOfDay()));
            ps.setTimestamp(4, Timestamp.valueOf(r.getCheckOut().atStartOfDay()));
            ps.setString(5, normalizeStatus(r.getStatus()));
            ps.setInt(6, r.getUpdatedByUserId() == null ? 0 : r.getUpdatedByUserId());
            ps.setInt(7, r.getReservationNo());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new RuntimeException("Updating reservation failed", e);
        }
    }

    @Override
    public boolean delete(int reservationId) {
        String sql = "DELETE FROM reservations WHERE reservation_id = ?";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, reservationId);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            // likely FK restrict from bills
            throw new RuntimeException("Cannot delete reservation (may have bills).", e);
        }
    }

    @Override
    public Reservation findById(int id) {

        String sql =
                "SELECT r.reservation_id, r.reservation_number, r.guest_id, r.room_id, r.check_in_date, r.check_out_date, r.status, " +
                        "g.full_name AS guest_name, g.contact_no AS guest_contact_no, g.email AS guest_email, " +
                        "rm.room_number, rm.status AS room_status, rt.room_type_id, rt.type_name, rt.price AS room_type_price " +
                        "FROM reservations r " +
                        "JOIN guests g ON g.guest_id = r.guest_id " +
                        "JOIN rooms rm ON rm.room_id = r.room_id " +
                        "JOIN room_types rt ON rt.room_type_id = rm.room_type_id " +
                        "WHERE r.reservation_id = ?";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Find reservation by id failed", e);
        }
    }

    @Override
    public List<Reservation> search(String q, String status) {

        StringBuilder sql = new StringBuilder(
                "SELECT r.reservation_id, r.reservation_number, r.guest_id, r.room_id, r.check_in_date, r.check_out_date, r.status, " +
                        "g.full_name AS guest_name, g.contact_no AS guest_contact_no, g.email AS guest_email, " +
                        "rm.room_number, rm.status AS room_status, rt.room_type_id, rt.type_name, rt.price AS room_type_price " +
                        "FROM reservations r " +
                        "JOIN guests g ON g.guest_id = r.guest_id " +
                        "JOIN rooms rm ON rm.room_id = r.room_id " +
                        "JOIN room_types rt ON rt.room_type_id = rm.room_type_id " +
                        "WHERE 1=1 "
        );

        List<Object> params = new ArrayList<>();

        if (q != null && !q.trim().isEmpty()) {
            sql.append(" AND (r.reservation_number LIKE ? OR g.full_name LIKE ? OR rm.room_number LIKE ? OR r.status LIKE ?) ");
            String like = "%" + q.trim() + "%";
            params.add(like);
            params.add(like);
            params.add(like);
            params.add(like);
        }

        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND LOWER(r.status) = ? ");
            params.add(status.trim().toLowerCase());
        }

        sql.append(" ORDER BY r.reservation_id DESC");

        List<Reservation> list = new ArrayList<>();

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Search reservations failed", e);
        }

        return list;
    }

    @Override
    public boolean hasOverlap(int roomId, LocalDate checkIn, LocalDate checkOut, Integer excludeReservationId) {

        String sql =
                "SELECT COUNT(*) " +
                        "FROM reservations " +
                        "WHERE room_id = ? " +
                        "AND LOWER(status) <> 'cancelled' " +
                        "AND check_in_date < ? " +
                        "AND check_out_date > ? " +
                        (excludeReservationId != null ? "AND reservation_id <> ? " : "");

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            int i = 1;
            ps.setInt(i++, roomId);
            ps.setTimestamp(i++, Timestamp.valueOf(checkOut.atStartOfDay())); // existing.start < new.end
            ps.setTimestamp(i++, Timestamp.valueOf(checkIn.atStartOfDay()));  // existing.end > new.start
            if (excludeReservationId != null) ps.setInt(i, excludeReservationId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Overlap check failed", e);
        }
    }

    private Reservation map(ResultSet rs) throws SQLException {

        Reservation r = new Reservation();
        r.setReservationNo(rs.getInt("reservation_id"));
        r.setReservationNumber(rs.getString("reservation_number"));
        r.setStatus(rs.getString("status"));

        Timestamp cin = rs.getTimestamp("check_in_date");
        Timestamp cout = rs.getTimestamp("check_out_date");
        if (cin != null) r.setCheckIn(cin.toLocalDateTime().toLocalDate());
        if (cout != null) r.setCheckOut(cout.toLocalDateTime().toLocalDate());

        Guest g = new Guest();
        g.setGuestId(rs.getInt("guest_id"));
        g.setFullName(rs.getString("guest_name"));
        g.setContactNo(rs.getString("guest_contact_no"));
        g.setEmail(rs.getString("guest_email"));
        r.setGuest(g);

        Room room = new Room();
        room.setRoomId(rs.getInt("room_id"));
        room.setRoomNumber(rs.getString("room_number"));
        room.setStatus(rs.getString("room_status"));
        room.setRoomTypeId(rs.getInt("room_type_id"));
        r.setRoom(room);

        r.setRoomTypePrice(rs.getDouble("room_type_price"));

        return r;
    }

    private String normalizeStatus(String status) {
        if (status == null) return "booked";
        String s = status.trim().toLowerCase();
        if (s.isEmpty()) return "booked";
        return s;
    }

    @Override
    public void updateRoomStatus(int roomId, String status, int updatedByUserId) {
        String sql = "UPDATE rooms SET status = ?, updated_by_user_id = ?, updated_at = NOW() WHERE room_id = ?";
        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, updatedByUserId);
            ps.setInt(3, roomId);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Updating room status failed", e);
        }
    }
}