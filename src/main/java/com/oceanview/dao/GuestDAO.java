package com.oceanview.dao;

import com.oceanview.entity.Guest;

import java.util.List;

public interface GuestDAO {

    int save(Guest guest);

    void update(Guest guest);

    boolean delete(int guestId);

    Guest findById(int id);

    List<Guest> findAll();

    List<Guest> search(String query);

}