package com.oceanview.service;

import com.oceanview.dto.RoomDTO;

import java.util.List;

public interface RoomService {

    int addRoom(RoomDTO dto);

    void updateRoom(RoomDTO dto);

    void deleteRoom(int id);

    List<RoomDTO> viewRooms();
}