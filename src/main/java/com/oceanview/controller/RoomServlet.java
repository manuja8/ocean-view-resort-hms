package com.oceanview.controller;

import com.oceanview.dto.RoomDTO;
import com.oceanview.service.RoomService;
import com.oceanview.service.impl.RoomServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/rooms")
public class RoomServlet extends HttpServlet {

    private RoomService roomService = new RoomServiceImpl();

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        List<RoomDTO> rooms = roomService.viewRooms();
        req.setAttribute("rooms", rooms);

        try {
            req.getRequestDispatcher("rooms/view-rooms.jsp").forward(req, res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        String action = req.getParameter("action");

        if ("add".equals(action)) {
            RoomDTO dto = new RoomDTO();
            dto.setRoomType(req.getParameter("type"));
            dto.setRate(Double.parseDouble(req.getParameter("price")));
            dto.setAvailabilityStatus("AVAILABLE");

            roomService.addRoom(dto);
        }

        res.sendRedirect("rooms");
    }
}