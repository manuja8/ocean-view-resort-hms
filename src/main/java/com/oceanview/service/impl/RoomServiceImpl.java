package com.oceanview.service.impl;

import com.oceanview.dao.RoomDAO;
import com.oceanview.dao.impl.RoomDAOImpl;
import com.oceanview.dto.RoomDTO;
import com.oceanview.entity.Room;
import com.oceanview.mapper.RoomMapper;
import com.oceanview.service.RoomService;

import java.util.ArrayList;
import java.util.List;

public class RoomServiceImpl implements RoomService {

    private RoomDAO roomDAO = new RoomDAOImpl();

    @Override
    public int addRoom(RoomDTO dto) {
        Room room = RoomMapper.toEntity(dto);
        return roomDAO.save(room);
    }

    @Override
    public void updateRoom(RoomDTO dto) {
        Room room = RoomMapper.toEntity(dto);
        roomDAO.update(room);
    }

    @Override
    public void deleteRoom(int id) {
        roomDAO.delete(id);
    }

    @Override
    public List<RoomDTO> viewRooms() {
        List<Room> rooms = roomDAO.findAll();
        List<RoomDTO> list = new ArrayList<>();

        for (Room r : rooms) {
            list.add(RoomMapper.toDTO(r));
        }
        return list;
    }
}