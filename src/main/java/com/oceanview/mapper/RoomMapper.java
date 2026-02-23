package com.oceanview.mapper;

import com.oceanview.dto.RoomDTO;
import com.oceanview.entity.Room;

public class RoomMapper {

    public static RoomDTO toDTO(Room room) {
        if (room == null) return null;

        RoomDTO dto = new RoomDTO();
        dto.setRoomId(room.getRoomId());
        dto.setRoomType(room.getRoomType());
        dto.setRate(room.getRate());
        dto.setAvailabilityStatus(room.getAvailabilityStatus());

        return dto;
    }

    public static Room toEntity(RoomDTO dto) {
        if (dto == null) return null;

        Room room = new Room();
        room.setRoomId(dto.getRoomId());
        room.setRoomType(dto.getRoomType());
        room.setRate(dto.getRate());
        room.setAvailabilityStatus(dto.getAvailabilityStatus());

        return room;
    }
}