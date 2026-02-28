package com.oceanview.service.impl;

import com.oceanview.dao.ReservationDAO;
import com.oceanview.dao.RoomDAO;
import com.oceanview.dao.impl.ReservationDAOImpl;
import com.oceanview.dao.impl.RoomDAOImpl;
import com.oceanview.dto.ReservationDTO;
import com.oceanview.entity.Reservation;
import com.oceanview.entity.Room;
import com.oceanview.mapper.ReservationMapper;
import com.oceanview.service.ReservationService;

import java.util.ArrayList;
import java.util.List;

public class ReservationServiceImpl implements ReservationService {

    private ReservationDAO reservationDAO = new ReservationDAOImpl();
    private RoomDAO roomDAO = new RoomDAOImpl();

    @Override
    public int createReservation(ReservationDTO dto) {

        Reservation reservation = ReservationMapper.toEntity(dto);

        int generatedId = reservationDAO.save(reservation);

        // BUSINESS LOGIC: mark room as BOOKED
        Room room = roomDAO.findById(dto.getRoom().getRoomId());
        if (room != null) {
            room.setAvailabilityStatus("BOOKED");
            roomDAO.update(room);
        }

        return generatedId;
    }

    @Override
    public void updateReservation(ReservationDTO dto) {
        Reservation reservation = ReservationMapper.toEntity(dto);
        reservationDAO.update(reservation);
    }

    @Override
    public void cancelReservation(int reservationNo) {

        Reservation reservation = reservationDAO.findById(reservationNo);

        if (reservation != null) {

            // Free the room
            Room room = reservation.getRoom();
            room.setAvailabilityStatus("AVAILABLE");
            roomDAO.update(room);

            // Update reservation status
            reservation.setStatus("CANCELLED");
            reservationDAO.update(reservation);
        }
    }

    @Override
    public ReservationDTO getReservationById(int reservationNo) {
        return ReservationMapper.toDTO(
                reservationDAO.findById(reservationNo)
        );
    }

    @Override
    public List<ReservationDTO> getAllReservations() {

        List<Reservation> list = reservationDAO.findAll();
        List<ReservationDTO> dtoList = new ArrayList<>();

        for (Reservation r : list) {
            dtoList.add(ReservationMapper.toDTO(r));
        }

        return dtoList;
    }
}