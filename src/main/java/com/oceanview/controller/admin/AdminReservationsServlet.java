package com.oceanview.controller.admin;

import com.oceanview.service.ReservationService;
import com.oceanview.service.impl.ReservationServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/reservations")
public class AdminReservationsServlet extends HttpServlet {

    private final ReservationService reservationService = new ReservationServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        String mode = req.getParameter("mode");
        if (!"delete".equalsIgnoreCase(mode)) {
            resp.sendRedirect(req.getContextPath() + "/staff/reservations");
            return;
        }

        try {
            int id = Integer.parseInt(req.getParameter("id"));
            reservationService.deleteReservation(id);
            req.getSession().setAttribute("flashSuccess", "Reservation deleted.");
        } catch (Exception ex) {
            req.getSession().setAttribute("flashError", ex.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/staff/reservations");
    }
}