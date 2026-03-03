package com.oceanview.dao.impl;

import com.oceanview.config.DBConnection;
import com.oceanview.dao.RoomDAO;
import com.oceanview.dto.RoomDTO;
import com.oceanview.dto.RoomTypeDTO;
import com.oceanview.entity.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAOImpl implements RoomDAO {

    @Override
    public List<RoomDTO> search(String q, String status) {

        StringBuilder sql = new StringBuilder(
                "SELECT r.room_id, r.room_number, r.room_type_id, r.status, " +
                        "       rt.type_name, rt.price " +
                        "FROM rooms r " +
                        "INNER JOIN room_types rt ON rt.room_type_id = r.room_type_id " +
                        "WHERE 1=1 "
        );

        List<Object> params = new ArrayList<>();

        if (q != null && !q.trim().isEmpty()) {
            sql.append(" AND r.room_number LIKE ? ");
            params.add("%" + q.trim() + "%");
        }

        if (status != null && !status.trim().isEmpty()) {
            sql.append(" AND r.status = ? ");
            params.add(status.trim().toLowerCase());
        }

        sql.append(" ORDER BY r.room_number ");

        List<RoomDTO> list = new ArrayList<>();

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RoomDTO dto = new RoomDTO();
                    dto.setRoomId(rs.getInt("room_id"));
                    dto.setRoomNumber(rs.getString("room_number"));
                    dto.setRoomTypeId(rs.getInt("room_type_id"));
                    dto.setStatus(rs.getString("status"));
                    dto.setRoomTypeName(rs.getString("type_name"));
                    dto.setPrice(rs.getBigDecimal("price"));
                    list.add(dto);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Room search failed", e);
        }

        return list;
    }

    @Override
    public RoomDTO findDTOById(int id) {

        String sql =
                "SELECT r.room_id, r.room_number, r.room_type_id, r.status, " +
                        "       rt.type_name, rt.price " +
                        "FROM rooms r " +
                        "INNER JOIN room_types rt ON rt.room_type_id = r.room_type_id " +
                        "WHERE r.room_id = ?";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    RoomDTO dto = new RoomDTO();
                    dto.setRoomId(rs.getInt("room_id"));
                    dto.setRoomNumber(rs.getString("room_number"));
                    dto.setRoomTypeId(rs.getInt("room_type_id"));
                    dto.setStatus(rs.getString("status"));
                    dto.setRoomTypeName(rs.getString("type_name"));
                    dto.setPrice(rs.getBigDecimal("price"));
                    return dto;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Find room by id failed", e);
        }

        return null;
    }

    @Override
    public int save(Room room, int createdByUserId) {

        String sql =
                "INSERT INTO rooms (room_number, room_type_id, status, created_by_user_id, created_at, updated_at) " +
                        "VALUES (?, ?, ?, ?, NOW(), NOW())";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, room.getRoomNumber());
            ps.setInt(2, room.getRoomTypeId());
            ps.setString(3, room.getStatus());
            ps.setInt(4, createdByUserId);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Create room failed (maybe duplicate room number?)", e);
        }
    }

    @Override
    public boolean update(Room room, int updatedByUserId) {

        String sql =
                "UPDATE rooms " +
                        "SET room_number = ?, room_type_id = ?, status = ?, updated_by_user_id = ?, updated_at = NOW() " +
                        "WHERE room_id = ?";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, room.getRoomNumber());
            ps.setInt(2, room.getRoomTypeId());
            ps.setString(3, room.getStatus());
            ps.setInt(4, updatedByUserId);
            ps.setInt(5, room.getRoomId());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new RuntimeException("Update room failed", e);
        }
    }

    @Override
    public boolean delete(int id) {

        String sql = "DELETE FROM rooms WHERE room_id = ?";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new RuntimeException("Delete room failed", e);
        }
    }

    @Override
    public List<RoomTypeDTO> findAllRoomTypes() {

        String sql = "SELECT room_type_id, type_name, price FROM room_types ORDER BY type_name";
        List<RoomTypeDTO> list = new ArrayList<>();

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                RoomTypeDTO t = new RoomTypeDTO();
                t.setRoomTypeId(rs.getInt("room_type_id"));
                t.setTypeName(rs.getString("type_name"));
                t.setPrice(rs.getBigDecimal("price"));
                list.add(t);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Load room types failed", e);
        }

        return list;
    }

    @Override
    public int countAllRooms() {
        String sql = "SELECT COUNT(*) FROM rooms";
        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            return rs.next() ? rs.getInt(1) : 0;

        } catch (SQLException e) {
            throw new RuntimeException("Count rooms failed", e);
        }
    }
}