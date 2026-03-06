package com.oceanview.service;

import com.oceanview.dto.RoomDTO;
import com.oceanview.dto.RoomTypeDTO;

import java.util.List;

public interface RoomService {

    List<RoomDTO> searchRooms(String q, String status);

    RoomDTO getRoomById(int id);

    List<RoomTypeDTO> getRoomTypes();

    int createRoom(RoomDTO dto, int createdByUserId);

    void updateRoom(RoomDTO dto, int updatedByUserId);

    void deleteRoom(int id);
}