package com.oceanview.mapper;

import com.oceanview.dto.RoomDTO;
import com.oceanview.entity.Room;

public class RoomMapper {

    public static Room toEntity(RoomDTO dto) {
        if (dto == null) return null;
        Room r = new Room();
        r.setRoomId(dto.getRoomId());
        r.setRoomNumber(dto.getRoomNumber());
        r.setRoomTypeId(dto.getRoomTypeId());
        r.setStatus(dto.getStatus());
        return r;
    }

    public static RoomDTO toDTO(Room room) {
        if (room == null) return null;
        RoomDTO dto = new RoomDTO();
        dto.setRoomId(room.getRoomId());
        dto.setRoomNumber(room.getRoomNumber());
        dto.setRoomTypeId(room.getRoomTypeId());
        dto.setStatus(room.getStatus());
        return dto;
    }
}