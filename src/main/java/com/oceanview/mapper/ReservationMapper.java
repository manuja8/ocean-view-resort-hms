package com.oceanview.mapper;

import com.oceanview.dto.ReservationDTO;
import com.oceanview.entity.Reservation;

public class ReservationMapper {

    public static ReservationDTO toDTO(Reservation reservation) {
        if (reservation == null) return null;

        ReservationDTO dto = new ReservationDTO();
        dto.setReservationNo(reservation.getReservationNo());
        dto.setGuest(GuestMapper.toDTO(reservation.getGuest()));
        dto.setRoom(RoomMapper.toDTO(reservation.getRoom()));
        dto.setCheckIn(reservation.getCheckIn());
        dto.setCheckOut(reservation.getCheckOut());
        dto.setStatus(reservation.getStatus());

        return dto;
    }

    public static Reservation toEntity(ReservationDTO dto) {
        if (dto == null) return null;

        Reservation reservation = new Reservation();
        reservation.setReservationNo(dto.getReservationNo());
        reservation.setGuest(GuestMapper.toEntity(dto.getGuest()));
        reservation.setRoom(RoomMapper.toEntity(dto.getRoom()));
        reservation.setCheckIn(dto.getCheckIn());
        reservation.setCheckOut(dto.getCheckOut());
        reservation.setStatus(dto.getStatus());

        return reservation;
    }
}