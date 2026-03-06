package com.oceanview.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("loggedUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String role = (String) session.getAttribute("role");
        if (role == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (role.equalsIgnoreCase("admin")) {
            req.getRequestDispatcher("/WEB-INF/views/dashboard/admin.jsp").forward(req, resp);
        } else if (role.equalsIgnoreCase("receptionist")) {
            req.getRequestDispatcher("/WEB-INF/views/dashboard/receptionist.jsp").forward(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}