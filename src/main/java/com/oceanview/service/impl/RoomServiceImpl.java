package com.oceanview.service.impl;

import com.oceanview.dao.RoomDAO;
import com.oceanview.dao.impl.RoomDAOImpl;
import com.oceanview.dto.RoomDTO;
import com.oceanview.dto.RoomTypeDTO;
import com.oceanview.entity.Room;
import com.oceanview.mapper.RoomMapper;
import com.oceanview.service.RoomService;

import java.util.List;

public class RoomServiceImpl implements RoomService {

    private final RoomDAO roomDAO = new RoomDAOImpl();

    @Override
    public List<RoomDTO> searchRooms(String q, String status) {
        return roomDAO.search(q, status);
    }

    @Override
    public RoomDTO getRoomById(int id) {
        return roomDAO.findDTOById(id);
    }

    @Override
    public List<RoomTypeDTO> getRoomTypes() {
        return roomDAO.findAllRoomTypes();
    }

    @Override
    public int createRoom(RoomDTO dto, int createdByUserId) {
        normalizeAndValidate(dto);
        Room entity = RoomMapper.toEntity(dto);
        return roomDAO.save(entity, createdByUserId);
    }

    @Override
    public void updateRoom(RoomDTO dto, int updatedByUserId) {
        normalizeAndValidate(dto);
        if (dto.getRoomId() <= 0) throw new IllegalArgumentException("Invalid room id");
        Room entity = RoomMapper.toEntity(dto);
        boolean ok = roomDAO.update(entity, updatedByUserId);
        if (!ok) throw new IllegalStateException("Room update failed (room not found?)");
    }

    @Override
    public void deleteRoom(int id) {
        if (id <= 0) throw new IllegalArgumentException("Invalid room id");
        boolean ok = roomDAO.delete(id);
        if (!ok) throw new IllegalStateException("Room delete failed (room not found?)");
    }

    private void normalizeAndValidate(RoomDTO dto) {
        if (dto.getRoomNumber() == null || dto.getRoomNumber().trim().isEmpty())
            throw new IllegalArgumentException("Room number is required");

        String rn = dto.getRoomNumber().trim();
        if (rn.length() > 10)
            throw new IllegalArgumentException("Room number must be 10 characters or less");
        dto.setRoomNumber(rn);

        if (dto.getRoomTypeId() <= 0)
            throw new IllegalArgumentException("Room type is required");

        if (dto.getStatus() == null) dto.setStatus("available");
        String st = dto.getStatus().trim().toLowerCase();

        if (!(st.equals("available") || st.equals("occupied") || st.equals("maintenance")))
            throw new IllegalArgumentException("Invalid status");
        dto.setStatus(st);
    }
}