package com.oceanview.controller.admin;

import com.oceanview.service.GuestService;
import com.oceanview.service.impl.GuestServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/guests")
public class AdminGuestsServlet extends HttpServlet {

    private final GuestService guestService = new GuestServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        String mode = req.getParameter("mode");
        if (!"delete".equalsIgnoreCase(mode)) {
            resp.sendRedirect(req.getContextPath() + "/staff/guests");
            return;
        }

        try {
            int id = Integer.parseInt(req.getParameter("id"));
            guestService.deleteGuest(id);
            req.getSession().setAttribute("flashSuccess", "Guest deleted.");
        } catch (Exception ex) {
            req.getSession().setAttribute("flashError", ex.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/staff/guests");
    }
}