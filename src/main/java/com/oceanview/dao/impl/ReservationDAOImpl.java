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

    private Connection con = DBConnection.getInstance().getConnection();

    @Override
    public int save(Reservation reservation) {
        try {
            String sql = "INSERT INTO reservations (guest_id, room_id, check_in, check_out, status) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, reservation.getGuest().getGuestId());
            ps.setInt(2, reservation.getRoom().getRoomId());
            ps.setDate(3, Date.valueOf(reservation.getCheckIn()));
            ps.setDate(4, Date.valueOf(reservation.getCheckOut()));
            ps.setString(5, reservation.getStatus());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public void update(Reservation reservation) {
        try {
            String sql = "UPDATE reservations SET guest_id=?, room_id=?, check_in=?, check_out=?, status=? WHERE reservation_id=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, reservation.getGuest().getGuestId());
            ps.setInt(2, reservation.getRoom().getRoomId());
            ps.setDate(3, Date.valueOf(reservation.getCheckIn()));
            ps.setDate(4, Date.valueOf(reservation.getCheckOut()));
            ps.setString(5, reservation.getStatus());
            ps.setInt(6, reservation.getReservationNo());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Reservation findById(int id) {
        try {
            String sql = "SELECT * FROM reservations WHERE reservation_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractReservation(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Reservation> findAll() {
        List<Reservation> list = new ArrayList<>();

        try {
            String sql = "SELECT * FROM reservations";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(extractReservation(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public List<Reservation> search(String keyword) {
        List<Reservation> list = new ArrayList<>();

        try {
            String sql = "SELECT * FROM reservations WHERE status LIKE ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(extractReservation(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    private Reservation extractReservation(ResultSet rs) throws SQLException {

        Reservation reservation = new Reservation();

        reservation.setReservationNo(rs.getInt("reservation_id"));

        Guest guest = new Guest();
        guest.setGuestId(rs.getInt("guest_id"));
        reservation.setGuest(guest);

        Room room = new Room();
        room.setRoomId(rs.getInt("room_id"));
        reservation.setRoom(room);

        reservation.setCheckIn(rs.getDate("check_in").toLocalDate());
        reservation.setCheckOut(rs.getDate("check_out").toLocalDate());
        reservation.setStatus(rs.getString("status"));

        return reservation;
    }
}