package com.oceanview.factory;

import com.oceanview.dto.GuestDTO;
import com.oceanview.dto.ReservationDTO;
import com.oceanview.dto.RoomDTO;
import com.oceanview.entity.Guest;
import com.oceanview.entity.Reservation;
import com.oceanview.entity.Room;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReservationFactory {


    public static String generateReservationNumber() {
        return "RSV" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
    }

    public static Reservation createNew(ReservationDTO dto, int createdByUserId) {
        Reservation r = new Reservation();

        r.setReservationNumber(generateReservationNumber());
        r.setStatus("booked");
        r.setCreatedByUserId(createdByUserId);

        Guest g = new Guest();
        GuestDTO gd = dto.getGuest();
        g.setGuestId(gd.getGuestId());
        r.setGuest(g);

        Room rm = new Room();
        RoomDTO rd = dto.getRoom();
        rm.setRoomId(rd.getRoomId());
        r.setRoom(rm);

        r.setCheckIn(dto.getCheckIn());
        r.setCheckOut(dto.getCheckOut());

        return r;
    }

    public static Reservation createForUpdate(ReservationDTO dto, int updatedByUserId) {
        Reservation r = new Reservation();

        r.setReservationNo(dto.getReservationNo());
        r.setReservationNumber(dto.getReservationNumber());
        r.setUpdatedByUserId(updatedByUserId);

        Guest g = new Guest();
        g.setGuestId(dto.getGuest().getGuestId());
        r.setGuest(g);

        Room rm = new Room();
        rm.setRoomId(dto.getRoom().getRoomId());
        r.setRoom(rm);

        r.setCheckIn(dto.getCheckIn());
        r.setCheckOut(dto.getCheckOut());
        r.setStatus(dto.getStatus());

        return r;
    }
}