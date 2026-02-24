package com.oceanview.controller;

import com.oceanview.dto.GuestDTO;
import com.oceanview.service.GuestService;
import com.oceanview.service.impl.GuestServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/guest")
public class GuestServlet extends HttpServlet {

    private GuestService guestService = new GuestServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("list".equals(action)) {
            List<GuestDTO> guests = guestService.getAllGuests();
            request.setAttribute("guestList", guests);
            request.getRequestDispatcher("/views/guest-list.jsp").forward(request, response);

        } else if ("edit".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            GuestDTO guest = guestService.getGuestById(id);
            request.setAttribute("guest", guest);
            request.getRequestDispatcher("/views/guest-form.jsp").forward(request, response);

        } else {
            response.sendRedirect("guest?action=list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("save".equals(action)) {

            GuestDTO guest = new GuestDTO();
            guest.setGuestId(parseInt(request.getParameter("guestId")));
            guest.setName(request.getParameter("fullName"));
            guest.setContactNumber(request.getParameter("contactNumber"));
            guest.setEmail(request.getParameter("email"));
            guest.setAddress(request.getParameter("address"));

            if (guest.getGuestId() == 0) {
                guestService.addGuest(guest);
            } else {
                guestService.updateGuest(guest);
            }

            response.sendRedirect("guest?action=list");

        } else if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            guestService.deleteGuest(id);
            response.sendRedirect("guest?action=list");
        }
    }

    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }
}