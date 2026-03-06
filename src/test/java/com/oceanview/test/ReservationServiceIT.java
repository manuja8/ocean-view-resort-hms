package com.oceanview.test;

import com.oceanview.dto.GuestDTO;
import com.oceanview.dto.ReservationDTO;
import com.oceanview.dto.RoomDTO;
import com.oceanview.service.impl.ReservationServiceImpl;
import com.oceanview.service.impl.RoomServiceImpl;
import com.oceanview.testutil.TestDb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationServiceIT extends BaseH2Test {

    private ReservationServiceImpl reservationService;
    private RoomServiceImpl roomService;

    @BeforeEach
    void setup() {
        TestDb.clean();
        TestDb.seedBasicData();
        reservationService = new ReservationServiceImpl();
        roomService = new RoomServiceImpl();
    }

    @Test
    void createReservation_shouldWork() {
        ReservationDTO dto = new ReservationDTO();
        GuestDTO guest = new GuestDTO();
        guest.setGuestId(1);

        RoomDTO room = new RoomDTO();
        room.setRoomId(1); // seeded room

        dto.setGuest(guest);
        dto.setRoom(room);
        dto.setCheckIn(LocalDate.parse("2026-03-10"));
        dto.setCheckOut(LocalDate.parse("2026-03-13"));
        dto.setStatus("booked");

        int id = reservationService.createReservation(dto, 1);
        assertTrue(id > 0);

        ReservationDTO saved = reservationService.getReservationById(id);
        assertNotNull(saved);
        assertEquals("booked", saved.getStatus());
    }

    @Test
    void createReservation_shouldRejectOverlap() {
        // seed first reservation
        TestDb.seedReservation(
                1, 1, 1,
                LocalDate.parse("2026-03-10"),
                LocalDate.parse("2026-03-13"),
                "booked", 1
        );

        ReservationDTO dto = new ReservationDTO();
        GuestDTO guest = new GuestDTO();
        guest.setGuestId(1);
        RoomDTO room = new RoomDTO();
        room.setRoomId(1);

        dto.setGuest(guest);
        dto.setRoom(room);
        dto.setCheckIn(LocalDate.parse("2026-03-12")); // overlaps
        dto.setCheckOut(LocalDate.parse("2026-03-14"));
        dto.setStatus("booked");

        assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(dto, 1));
    }

    @Test
    void updateReservation_bookedToCheckedIn_shouldOccupyRoom() {
        TestDb.seedReservation(
                1, 1, 1,
                LocalDate.parse("2026-03-10"),
                LocalDate.parse("2026-03-13"),
                "booked", 1
        );

        ReservationDTO existing = reservationService.getReservationById(1);
        existing.setStatus("checked_in");

        reservationService.updateReservation(existing, 1);

        // room status should become occupied
        assertEquals("occupied", roomService.getRoomById(1).getStatus());
    }

    @Test
    void cancelReservation_shouldSetCancelled() {
        TestDb.seedReservation(
                1, 1, 1,
                LocalDate.parse("2026-03-10"),
                LocalDate.parse("2026-03-13"),
                "booked", 1
        );

        reservationService.cancelReservation(1, 1);

        ReservationDTO after = reservationService.getReservationById(1);
        assertEquals("cancelled", after.getStatus());
    }
}