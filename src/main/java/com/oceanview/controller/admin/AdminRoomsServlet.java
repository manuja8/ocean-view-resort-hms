package com.oceanview.controller.admin;

import com.oceanview.dto.RoomDTO;
import com.oceanview.service.RoomService;
import com.oceanview.service.impl.RoomServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/rooms")
public class AdminRoomsServlet extends HttpServlet {

    private final RoomService roomService = new RoomServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String mode = req.getParameter("mode");
        if (mode == null || mode.isBlank()) mode = "list";

        // flash messages (PRG)
        HttpSession session = req.getSession(false);
        if (session != null) {
            Object success = session.getAttribute("flashSuccess");
            Object error = session.getAttribute("flashError");
            if (success != null) {
                req.setAttribute("success", success);
                session.removeAttribute("flashSuccess");
            }
            if (error != null) {
                req.setAttribute("error", error);
                session.removeAttribute("flashError");
            }
        }

        if ("create".equalsIgnoreCase(mode)) {
            req.setAttribute("mode", "create");
            req.setAttribute("roomTypes", roomService.getRoomTypes());
            req.setAttribute("room", new RoomDTO());
            req.getRequestDispatcher("/WEB-INF/views/admin/rooms/form.jsp").forward(req, resp);
            return;
        }

        if ("edit".equalsIgnoreCase(mode)) {
            int id = Integer.parseInt(req.getParameter("id"));
            RoomDTO room = roomService.getRoomById(id);
            if (room == null) {
                req.getSession().setAttribute("flashError", "Room not found.");
                resp.sendRedirect(req.getContextPath() + "/admin/rooms");
                return;
            }

            req.setAttribute("mode", "edit");
            req.setAttribute("roomTypes", roomService.getRoomTypes());
            req.setAttribute("room", room);
            req.getRequestDispatcher("/WEB-INF/views/admin/rooms/form.jsp").forward(req, resp);
            return;
        }

        // list
        String q = req.getParameter("q");
        String status = req.getParameter("status");

        req.setAttribute("rooms", roomService.searchRooms(q, status));
        req.getRequestDispatcher("/WEB-INF/views/admin/rooms/list.jsp").forward(req, resp);
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
            if ("delete".equalsIgnoreCase(mode)) {
                int id = Integer.parseInt(req.getParameter("id"));
                roomService.deleteRoom(id);
                req.getSession().setAttribute("flashSuccess", "Room deleted.");
                resp.sendRedirect(req.getContextPath() + "/admin/rooms");
                return;
            }

            // create/update
            RoomDTO dto = new RoomDTO();
            dto.setRoomNumber(req.getParameter("roomNumber"));
            dto.setRoomTypeId(Integer.parseInt(req.getParameter("roomTypeId")));
            dto.setStatus(req.getParameter("status"));

            if ("edit".equalsIgnoreCase(mode)) {
                dto.setRoomId(Integer.parseInt(req.getParameter("id")));
                roomService.updateRoom(dto, userId);
                req.getSession().setAttribute("flashSuccess", "Room updated.");
            } else {
                roomService.createRoom(dto, userId);
                req.getSession().setAttribute("flashSuccess", "Room created.");
            }

            resp.sendRedirect(req.getContextPath() + "/admin/rooms");

        } catch (Exception ex) {
            // show form again with error + dropdown
            req.setAttribute("error", ex.getMessage());
            req.setAttribute("roomTypes", roomService.getRoomTypes());
            req.setAttribute("room", buildRoomFromRequest(req));
            req.setAttribute("mode", mode.isBlank() ? "create" : mode);

            req.getRequestDispatcher("/WEB-INF/views/admin/rooms/form.jsp").forward(req, resp);
        }
    }

    private RoomDTO buildRoomFromRequest(HttpServletRequest req) {
        RoomDTO dto = new RoomDTO();
        dto.setRoomNumber(req.getParameter("roomNumber"));
        dto.setStatus(req.getParameter("status"));
        try {
            dto.setRoomTypeId(Integer.parseInt(req.getParameter("roomTypeId")));
        } catch (Exception ignored) {
        }
        try {
            dto.setRoomId(Integer.parseInt(req.getParameter("id")));
        } catch (Exception ignored) {
        }
        return dto;
    }
}