package com.oceanview.controller.staff;

import com.oceanview.dto.ComplaintDTO;
import com.oceanview.service.ComplaintService;
import com.oceanview.service.GuestService;
import com.oceanview.service.impl.ComplaintServiceImpl;
import com.oceanview.service.impl.GuestServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/staff/complaints")
public class StaffComplaintsServlet extends HttpServlet {

    private final ComplaintService complaintService = new ComplaintServiceImpl();
    private final GuestService guestService = new GuestServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String mode = req.getParameter("mode");
        if (mode == null || mode.isBlank()) mode = "list";

        // flash
        if (req.getSession().getAttribute("flashSuccess") != null) {
            req.setAttribute("success", req.getSession().getAttribute("flashSuccess"));
            req.getSession().removeAttribute("flashSuccess");
        }
        if (req.getSession().getAttribute("flashError") != null) {
            req.setAttribute("error", req.getSession().getAttribute("flashError"));
            req.getSession().removeAttribute("flashError");
        }

        if ("create".equalsIgnoreCase(mode)) {
            req.setAttribute("mode", "create");
            req.setAttribute("complaint", new ComplaintDTO());
            req.setAttribute("guests", guestService.getAllGuests(null));
            req.getRequestDispatcher("/WEB-INF/views/staff/complaints/form.jsp").forward(req, resp);
            return;
        }

        if ("edit".equalsIgnoreCase(mode)) {
            int id = Integer.parseInt(req.getParameter("id"));
            ComplaintDTO dto = complaintService.getById(id);
            if (dto == null) {
                req.getSession().setAttribute("flashError", "Complaint not found.");
                resp.sendRedirect(req.getContextPath() + "/staff/complaints");
                return;
            }
            req.setAttribute("mode", "edit");
            req.setAttribute("complaint", dto);
            req.setAttribute("guests", guestService.getAllGuests(null));
            req.getRequestDispatcher("/WEB-INF/views/staff/complaints/form.jsp").forward(req, resp);
            return;
        }

        // list
        String q = req.getParameter("q");
        String status = req.getParameter("status");
        String priority = req.getParameter("priority");

        req.setAttribute("complaints", complaintService.search(q, status, priority));
        req.getRequestDispatcher("/WEB-INF/views/staff/complaints/list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        Integer userId = (Integer) req.getSession().getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String mode = req.getParameter("mode");
        if (mode == null) mode = "create";

        try {
            ComplaintDTO dto = fromRequest(req);

            if ("edit".equalsIgnoreCase(mode)) {
                dto.setComplaintId(Integer.parseInt(req.getParameter("id")));
                complaintService.update(dto, userId);
                req.getSession().setAttribute("flashSuccess", "Complaint updated.");
            } else {
                complaintService.create(dto, userId);
                req.getSession().setAttribute("flashSuccess", "Complaint submitted.");
            }

            resp.sendRedirect(req.getContextPath() + "/staff/complaints");

        } catch (Exception ex) {
            req.setAttribute("error", ex.getMessage());
            req.setAttribute("mode", mode);
            req.setAttribute("complaint", fromRequest(req));
            req.setAttribute("guests", guestService.getAllGuests(null));
            req.getRequestDispatcher("/WEB-INF/views/staff/complaints/form.jsp").forward(req, resp);
        }
    }

    private ComplaintDTO fromRequest(HttpServletRequest req) {
        ComplaintDTO dto = new ComplaintDTO();
        dto.setGuestId(Integer.parseInt(req.getParameter("guestId")));

        String rid = req.getParameter("reservationId");
        if (rid != null && !rid.trim().isEmpty()) {
            try {
                dto.setReservationId(Integer.parseInt(rid.trim()));
            } catch (Exception ignored) {
            }
        }

        dto.setSubject(req.getParameter("subject"));
        dto.setDescription(req.getParameter("description"));
        dto.setPriority(req.getParameter("priority"));
        dto.setStatus(req.getParameter("status"));
        return dto;
    }
}