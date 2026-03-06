package com.oceanview.dao;

import com.oceanview.dto.RoomDTO;
import com.oceanview.dto.RoomTypeDTO;
import com.oceanview.entity.Room;

import java.util.List;

public interface RoomDAO {


    List<RoomDTO> search(String q, String status);


    RoomDTO findDTOById(int id);


    int save(Room room, int createdByUserId);

    boolean update(Room room, int updatedByUserId);

    boolean delete(int id);

    int countAllRooms();

   
    List<RoomTypeDTO> findAllRoomTypes();
}