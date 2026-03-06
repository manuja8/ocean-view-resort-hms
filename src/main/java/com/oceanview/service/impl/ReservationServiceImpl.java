package com.oceanview.service.impl;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.dao.impl.ReservationDAOImpl;
import com.oceanview.dto.ReservationDTO;
import com.oceanview.entity.Reservation;
import com.oceanview.factory.ReservationFactory;
import com.oceanview.mapper.ReservationMapper;
import com.oceanview.service.ReservationService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationServiceImpl implements ReservationService {

    private final ReservationDAO reservationDAO = new ReservationDAOImpl();

    @Override
    public int createReservation(ReservationDTO dto, int createdByUserId) {
        validate(dto);

        int roomId = dto.getRoom().getRoomId();
        LocalDate in = dto.getCheckIn();
        LocalDate out = dto.getCheckOut();

        if (reservationDAO.hasOverlap(roomId, in, out, null)) {
            throw new IllegalArgumentException("This room already has a reservation overlapping the selected dates.");
        }


        Reservation r = ReservationFactory.createNew(dto, createdByUserId);


        try {
            return reservationDAO.save(r);
        } catch (RuntimeException ex) {
            r.setReservationNumber(ReservationFactory.generateReservationNumber());
            return reservationDAO.save(r);
        }
    }

    @Override
    public void updateReservation(ReservationDTO dto, int updatedByUserId) {
        validate(dto);


        Reservation current = reservationDAO.findById(dto.getReservationNo());
        if (current == null) throw new IllegalArgumentException("Reservation not found");

        String oldStatus = (current.getStatus() == null || current.getStatus().isBlank())
                ? "booked" : current.getStatus().trim().toLowerCase();

        String newStatus = dto.getStatus();

        int oldRoomId = current.getRoom().getRoomId();
        int newRoomId = dto.getRoom().getRoomId();


        if (oldStatus.equals("checked_out") || oldStatus.equals("cancelled")) {
            throw new IllegalStateException("Completed/cancelled reservations cannot be modified.");
        }


        if (oldStatus.equals("checked_in")) {
            dto.getRoom().setRoomId(oldRoomId);
            dto.setCheckIn(current.getCheckIn());
            newRoomId = oldRoomId;
        }


        boolean allowed =
                (oldStatus.equals("booked") && (newStatus.equals("booked") || newStatus.equals("checked_in") || newStatus.equals("cancelled")))
                        || (oldStatus.equals("checked_in") && (newStatus.equals("checked_in") || newStatus.equals("checked_out")));

        if (!allowed) {
            throw new IllegalStateException("Invalid status change: " + oldStatus + " -> " + newStatus);
        }


        int roomId = dto.getRoom().getRoomId();
        LocalDate in = dto.getCheckIn();
        LocalDate out = dto.getCheckOut();

        if (reservationDAO.hasOverlap(roomId, in, out, dto.getReservationNo())) {
            throw new IllegalArgumentException("This room already has a reservation overlapping the selected dates.");
        }


        Reservation r = ReservationFactory.createForUpdate(dto, updatedByUserId);
        boolean ok = reservationDAO.update(r);
        if (!ok) throw new IllegalStateException("Reservation update failed (not found?)");


        if (oldStatus.equals("booked") && newStatus.equals("checked_in")) {
            reservationDAO.updateRoomStatus(newRoomId, "occupied", updatedByUserId);
        }

        if (oldStatus.equals("checked_in") && newStatus.equals("checked_out")) {
            reservationDAO.updateRoomStatus(newRoomId, "available", updatedByUserId);
        }
    }

    @Override
    public void cancelReservation(int reservationNo, int updatedByUserId) {
        Reservation r = reservationDAO.findById(reservationNo);
        if (r == null) throw new IllegalArgumentException("Reservation not found");

        ReservationDTO dto = ReservationMapper.toDTO(r);
        dto.setStatus("cancelled");
        dto.setReservationNo(reservationNo);

        updateReservation(dto, updatedByUserId);
    }

    @Override
    public void deleteReservation(int reservationNo) {
        boolean ok = reservationDAO.delete(reservationNo);
        if (!ok) throw new IllegalStateException("Reservation delete failed (not found?)");
    }

    @Override
    public ReservationDTO getReservationById(int reservationNo) {
        return ReservationMapper.toDTO(reservationDAO.findById(reservationNo));
    }

    @Override
    public List<ReservationDTO> searchReservations(String q, String status) {
        return reservationDAO.search(q, status).stream()
                .map(ReservationMapper::toDTO)
                .collect(Collectors.toList());
    }

    private void validate(ReservationDTO dto) {
        if (dto.getGuest() == null || dto.getGuest().getGuestId() <= 0)
            throw new IllegalArgumentException("Guest is required");
        if (dto.getRoom() == null || dto.getRoom().getRoomId() <= 0)
            throw new IllegalArgumentException("Room is required");
        if (dto.getCheckIn() == null || dto.getCheckOut() == null)
            throw new IllegalArgumentException("Check-in and Check-out dates are required");
        if (!dto.getCheckOut().isAfter(dto.getCheckIn()))
            throw new IllegalArgumentException("Check-out must be after Check-in");

       
        if (dto.getStatus() == null || dto.getStatus().isBlank()) dto.setStatus("booked");
        String s = dto.getStatus().trim().toLowerCase();

        if (!(s.equals("booked") || s.equals("checked_in") || s.equals("checked_out") || s.equals("cancelled"))) {
            throw new IllegalArgumentException("Invalid reservation status");
        }
        dto.setStatus(s);
    }


}