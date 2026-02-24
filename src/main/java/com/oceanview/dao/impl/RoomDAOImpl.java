package com.oceanview.dao.impl;

import com.oceanview.config.DBConnection;
import com.oceanview.dao.RoomDAO;
import com.oceanview.entity.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAOImpl implements RoomDAO {

    private Connection con = DBConnection.getInstance().getConnection();

    @Override
    public int save(Room room) {
        try {
            String sql = "INSERT INTO rooms (type, price, status) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, room.getRoomType());
            ps.setDouble(2, room.getRate());
            ps.setString(3, room.getAvailabilityStatus());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void update(Room room) {
        try {
            String sql = "UPDATE rooms SET type=?, price=?, status=? WHERE room_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, room.getRoomType());
            ps.setDouble(2, room.getRate());
            ps.setString(3, room.getAvailabilityStatus());
            ps.setInt(4, room.getRoomId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        try {
            String sql = "DELETE FROM rooms WHERE room_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Room findById(int id) {
        try {
            String sql = "SELECT * FROM rooms WHERE room_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Room room = new Room();
                room.setRoomId(rs.getInt("room_id"));
                room.setRoomType(rs.getString("type"));
                room.setRate(rs.getDouble("price"));
                room.setAvailabilityStatus(rs.getString("status"));
                return room;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Room> findAll() {
        List<Room> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM rooms";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Room room = new Room();
                room.setRoomId(rs.getInt("room_id"));
                room.setRoomType(rs.getString("type"));
                room.setRate(rs.getDouble("price"));
                room.setAvailabilityStatus(rs.getString("status"));
                list.add(room);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Room> findAvailable() {
        List<Room> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM rooms WHERE status='AVAILABLE'";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Room room = new Room();
                room.setRoomId(rs.getInt("room_id"));
                room.setRoomType(rs.getString("type"));
                room.setRate(rs.getDouble("price"));
                room.setAvailabilityStatus(rs.getString("status"));
                list.add(room);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}