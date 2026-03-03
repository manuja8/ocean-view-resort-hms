package com.oceanview.controller.staff;

import com.oceanview.dto.GuestDTO;
import com.oceanview.service.GuestService;
import com.oceanview.service.impl.GuestServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/staff/guests")
public class StaffGuestsServlet extends HttpServlet {

    private final GuestService guestService = new GuestServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String mode = req.getParameter("mode");
        if (mode == null || mode.isBlank()) mode = "list";

        if ("create".equalsIgnoreCase(mode)) {
            req.setAttribute("mode", "create");
            req.setAttribute("guest", new GuestDTO());
            req.getRequestDispatcher("/WEB-INF/views/staff/guests/form.jsp").forward(req, resp);
            return;
        }

        if ("edit".equalsIgnoreCase(mode)) {
            int id = Integer.parseInt(req.getParameter("id"));
            GuestDTO dto = guestService.getGuestById(id);
            if (dto == null) {
                req.setAttribute("error", "Guest not found.");
                resp.sendRedirect(req.getContextPath() + "/staff/guests");
                return;
            }
            req.setAttribute("mode", "edit");
            req.setAttribute("guest", dto);
            req.getRequestDispatcher("/WEB-INF/views/staff/guests/form.jsp").forward(req, resp);
            return;
        }

        String q = req.getParameter("q");
        req.setAttribute("guestList", guestService.getAllGuests(q));
        req.getRequestDispatcher("/WEB-INF/views/staff/guests/list.jsp").forward(req, resp);
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
            GuestDTO dto = fromRequest(req);

            if ("edit".equalsIgnoreCase(mode)) {
                dto.setGuestId(Integer.parseInt(req.getParameter("id")));
                guestService.updateGuest(dto, userId);
                req.getSession().setAttribute("flashSuccess", "Guest updated.");
            } else {
                guestService.addGuest(dto, userId);
                req.getSession().setAttribute("flashSuccess", "Guest added.");
            }

            resp.sendRedirect(req.getContextPath() + "/staff/guests");
            return;

        } catch (Exception ex) {
            req.setAttribute("error", ex.getMessage());
            req.setAttribute("mode", mode.isBlank() ? "create" : mode);
            req.setAttribute("guest", fromRequest(req));
            req.getRequestDispatcher("/WEB-INF/views/staff/guests/form.jsp").forward(req, resp);
        }
    }

    private GuestDTO fromRequest(HttpServletRequest req) {
        GuestDTO dto = new GuestDTO();
        dto.setFullName(req.getParameter("fullName"));
        dto.setGender(req.getParameter("gender"));
        dto.setDateOfBirth(req.getParameter("dateOfBirth"));
        dto.setAddress(req.getParameter("address"));
        dto.setContactNo(req.getParameter("contactNo"));
        dto.setEmail(req.getParameter("email"));
        dto.setIdentificationType(req.getParameter("identificationType"));
        dto.setIdentificationNo(req.getParameter("identificationNo"));
        return dto;
    }
}