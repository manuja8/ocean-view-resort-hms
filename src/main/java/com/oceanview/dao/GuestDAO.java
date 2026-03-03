package com.oceanview.dao;

import com.oceanview.entity.Guest;

import java.util.List;

public interface GuestDAO {
    int save(Guest guest, int createdByUserId);

    boolean update(Guest guest, int updatedByUserId);

    boolean delete(int guestId);

    Guest findById(int id);

    List<Guest> findAll();

    List<Guest> search(String q);
}