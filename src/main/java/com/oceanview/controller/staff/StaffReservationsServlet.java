package com.oceanview.controller.staff;

import com.oceanview.dto.GuestDTO;
import com.oceanview.dto.ReservationDTO;
import com.oceanview.dto.RoomDTO;
import com.oceanview.service.GuestService;
import com.oceanview.service.ReservationService;
import com.oceanview.service.RoomService;
import com.oceanview.service.impl.GuestServiceImpl;
import com.oceanview.service.impl.ReservationServiceImpl;
import com.oceanview.service.impl.RoomServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/staff/reservations")
public class StaffReservationsServlet extends HttpServlet {

    private final ReservationService reservationService = new ReservationServiceImpl();
    private final GuestService guestService = new GuestServiceImpl();
    private final RoomService roomService = new RoomServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String mode = req.getParameter("mode");
        if (mode == null || mode.isBlank()) mode = "list";

        if ("create".equalsIgnoreCase(mode)) {
            req.setAttribute("mode", "create");
            req.setAttribute("reservation", new ReservationDTO());
            req.setAttribute("guests", guestService.getAllGuests(null));
            req.setAttribute("rooms", roomService.searchRooms(null, null));
            req.getRequestDispatcher("/WEB-INF/views/staff/reservations/form.jsp").forward(req, resp);
            return;
        }

        if ("edit".equalsIgnoreCase(mode)) {
            int id = Integer.parseInt(req.getParameter("id"));
            ReservationDTO dto = reservationService.getReservationById(id);
            if (dto == null) {
                req.getSession().setAttribute("flashError", "Reservation not found.");
                resp.sendRedirect(req.getContextPath() + "/staff/reservations");
                return;
            }
            req.setAttribute("mode", "edit");
            req.setAttribute("reservation", dto);
            req.setAttribute("guests", guestService.getAllGuests(null));
            req.setAttribute("rooms", roomService.searchRooms(null, null));
            req.getRequestDispatcher("/WEB-INF/views/staff/reservations/form.jsp").forward(req, resp);
            return;
        }

        // list
        String q = req.getParameter("q");
        String status = req.getParameter("status");

        req.setAttribute("reservationList", reservationService.searchReservations(q, status));
        req.getRequestDispatcher("/WEB-INF/views/staff/reservations/list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        String mode = req.getParameter("mode");
        if (mode == null) mode = "";

        Integer userId = (Integer) req.getSession().getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            if ("cancel".equalsIgnoreCase(mode)) {
                int id = Integer.parseInt(req.getParameter("id"));
                reservationService.cancelReservation(id, userId);
                req.getSession().setAttribute("flashSuccess", "Reservation cancelled.");
                resp.sendRedirect(req.getContextPath() + "/staff/reservations");
                return;
            }

            ReservationDTO dto = fromRequest(req);

            if ("edit".equalsIgnoreCase(mode)) {
                dto.setReservationNo(Integer.parseInt(req.getParameter("id")));
                reservationService.updateReservation(dto, userId);
                req.getSession().setAttribute("flashSuccess", "Reservation updated.");
            } else {
                reservationService.createReservation(dto, userId);
                req.getSession().setAttribute("flashSuccess", "Reservation created.");
            }

            resp.sendRedirect(req.getContextPath() + "/staff/reservations");

        } catch (Exception ex) {
            req.setAttribute("error", ex.getMessage());
            req.setAttribute("mode", mode.isBlank() ? "create" : mode);
            req.setAttribute("reservation", fromRequest(req));
            req.setAttribute("guests", guestService.getAllGuests(null));
            req.setAttribute("rooms", roomService.searchRooms(null, null));
            req.getRequestDispatcher("/WEB-INF/views/staff/reservations/form.jsp").forward(req, resp);
        }
    }

    private ReservationDTO fromRequest(HttpServletRequest req) {
        ReservationDTO dto = new ReservationDTO();

        GuestDTO g = new GuestDTO();
        g.setGuestId(Integer.parseInt(req.getParameter("guestId")));
        dto.setGuest(g);

        RoomDTO r = new RoomDTO();
        r.setRoomId(Integer.parseInt(req.getParameter("roomId")));
        dto.setRoom(r);

        dto.setCheckIn(LocalDate.parse(req.getParameter("checkIn")));
        dto.setCheckOut(LocalDate.parse(req.getParameter("checkOut")));

        String status = req.getParameter("status");
        dto.setStatus((status == null || status.isBlank()) ? "booked" : status);

        return dto;
    }
}
