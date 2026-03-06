package com.oceanview.controller.staff;

import com.oceanview.observer.NotificationCenter;
import com.oceanview.observer.NotificationMessage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/staff/notifications")
public class StaffNotificationsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String role = (String) req.getSession().getAttribute("role");
        if (role == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        NotificationCenter nc = NotificationCenter.getInstance();

        List<NotificationMessage> list = new ArrayList<>();
        if ("admin".equalsIgnoreCase(role)) {
            list.addAll(nc.getAll());
        } else {
            list.addAll(nc.getForAudience("staff"));
        }

        req.setAttribute("notifications", list);
        req.getRequestDispatcher("/WEB-INF/views/staff/notifications/list.jsp").forward(req, resp);
    }
}