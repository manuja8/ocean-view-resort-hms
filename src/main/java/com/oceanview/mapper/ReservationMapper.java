package com.oceanview.mapper;

import com.oceanview.dto.ReservationDTO;
import com.oceanview.entity.Reservation;

public class ReservationMapper {

    public static ReservationDTO toDTO(Reservation r) {
        if (r == null) return null;

        ReservationDTO dto = new ReservationDTO();
        dto.setReservationNo(r.getReservationNo());
        dto.setReservationNumber(r.getReservationNumber());
        dto.setGuest(GuestMapper.toDTO(r.getGuest()));
        dto.setRoom(RoomMapper.toDTO(r.getRoom()));
        dto.setCheckIn(r.getCheckIn());
        dto.setCheckOut(r.getCheckOut());
        dto.setStatus(r.getStatus());
        return dto;
    }

    public static Reservation toEntity(ReservationDTO dto) {
        if (dto == null) return null;

        Reservation r = new Reservation();
        r.setReservationNo(dto.getReservationNo());
        r.setReservationNumber(dto.getReservationNumber());
        r.setGuest(GuestMapper.toEntity(dto.getGuest()));
        r.setRoom(RoomMapper.toEntity(dto.getRoom()));
        r.setCheckIn(dto.getCheckIn());
        r.setCheckOut(dto.getCheckOut());
        r.setStatus(dto.getStatus());
        return r;
    }
}