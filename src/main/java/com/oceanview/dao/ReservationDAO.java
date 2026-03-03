package com.oceanview.dao;

import com.oceanview.entity.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationDAO {

    int save(Reservation reservation);

    boolean update(Reservation reservation);

    boolean delete(int reservationId);

    Reservation findById(int id);

    List<Reservation> search(String q, String status);

    boolean hasOverlap(int roomId, LocalDate checkIn, LocalDate checkOut, Integer excludeReservationId);
}