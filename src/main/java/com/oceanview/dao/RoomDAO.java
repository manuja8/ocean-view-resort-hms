package com.oceanview.dao;

import com.oceanview.entity.Room;

import java.util.List;

public interface RoomDAO {

    int save(Room room);

    void update(Room room);

    void delete(int id);

    Room findById(int id);

    List<Room> findAll();

    List<Room> findAvailable();
}