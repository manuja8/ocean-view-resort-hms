package com.oceanview.service;

import com.oceanview.dto.ReservationDTO;

import java.util.List;

public interface ReservationService {

    int createReservation(ReservationDTO dto);

    void updateReservation(ReservationDTO dto);

    void cancelReservation(int reservationNo);

    ReservationDTO getReservationById(int reservationNo);

    List<ReservationDTO> getAllReservations();
}