package com.oceanview.dao;

import com.oceanview.entity.Reservation;

import java.util.List;

public interface ReservationDAO {

    int save(Reservation reservation);

    void update(Reservation reservation);

    Reservation findById(int id);

    List<Reservation> findAll();

    List<Reservation> search(String keyword);
}