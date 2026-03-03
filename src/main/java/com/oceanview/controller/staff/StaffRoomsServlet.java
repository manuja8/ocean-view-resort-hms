package com.oceanview.controller.staff;

import com.oceanview.service.RoomService;
import com.oceanview.service.impl.RoomServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/staff/rooms")
public class StaffRoomsServlet extends HttpServlet {

    private final RoomService roomService = new RoomServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            String q = req.getParameter("q");
            String status = req.getParameter("status");

            req.setAttribute("rooms", roomService.searchRooms(q, status)); // reuse existing service
            req.getRequestDispatcher("/WEB-INF/views/staff/rooms/list.jsp").forward(req, resp);

        } catch (Exception ex) {
            req.setAttribute("error", ex.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/staff/rooms/list.jsp").forward(req, resp);
        }
    }
}