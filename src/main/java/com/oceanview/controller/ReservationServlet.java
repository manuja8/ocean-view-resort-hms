package com.oceanview.controller;

import com.oceanview.dto.GuestDTO;
import com.oceanview.dto.ReservationDTO;
import com.oceanview.dto.RoomDTO;
import com.oceanview.service.ReservationService;
import com.oceanview.service.impl.ReservationServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/reservation")
public class ReservationServlet extends HttpServlet {

    private ReservationService reservationService = new ReservationServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) action = "list";

        switch (action) {

            case "new":
                request.getRequestDispatcher("views/reservation/reservation-form.jsp")
                        .forward(request, response);
                break;

            case "cancel":
                int id = Integer.parseInt(request.getParameter("id"));
                reservationService.cancelReservation(id);
                response.sendRedirect("reservation");
                break;

            default:
                List<ReservationDTO> list = reservationService.getAllReservations();
                request.setAttribute("reservationList", list);
                request.getRequestDispatcher("views/reservation/reservation-list.jsp")
                        .forward(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ReservationDTO dto = new ReservationDTO();

        GuestDTO guest = new GuestDTO();
        guest.setGuestId(Integer.parseInt(request.getParameter("guestId")));

        RoomDTO room = new RoomDTO();
        room.setRoomId(Integer.parseInt(request.getParameter("roomId")));

        dto.setGuest(guest);
        dto.setRoom(room);
        dto.setCheckIn(LocalDate.parse(request.getParameter("checkIn")));
        dto.setCheckOut(LocalDate.parse(request.getParameter("checkOut")));
        dto.setStatus("CONFIRMED");

        reservationService.createReservation(dto);

        response.sendRedirect("reservation");
    }
}