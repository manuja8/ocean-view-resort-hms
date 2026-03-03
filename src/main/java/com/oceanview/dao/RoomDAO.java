package com.oceanview.dao;

import com.oceanview.dto.RoomDTO;
import com.oceanview.dto.RoomTypeDTO;
import com.oceanview.entity.Room;

import java.util.List;

public interface RoomDAO {

    // Admin list (supports search + filter)
    List<RoomDTO> search(String q, String status);

    // Edit load
    RoomDTO findDTOById(int id);

    // Create / Update / Delete
    int save(Room room, int createdByUserId);

    boolean update(Room room, int updatedByUserId);

    boolean delete(int id);

    int countAllRooms();

    // Dropdown for room types in form.jsp
    List<RoomTypeDTO> findAllRoomTypes();
}