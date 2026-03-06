package com.oceanview.service;

import com.oceanview.dto.ReservationDTO;

import java.util.List;

public interface ReservationService {

    int createReservation(ReservationDTO dto, int createdByUserId);

    void updateReservation(ReservationDTO dto, int updatedByUserId);

    void cancelReservation(int reservationNo, int updatedByUserId);

    void deleteReservation(int reservationNo); // admin-only servlet calls this

    ReservationDTO getReservationById(int reservationNo);

    List<ReservationDTO> searchReservations(String q, String status);
}