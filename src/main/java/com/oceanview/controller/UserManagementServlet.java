package com.oceanview.controller;

import com.oceanview.dto.UserDTO;
import com.oceanview.service.UserManagementService;
import com.oceanview.service.impl.UserManagementServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/dashboard/admin/user-management")
public class UserManagementServlet extends HttpServlet {

    private UserManagementService userService = new UserManagementServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String search = req.getParameter("search");
        List<UserDTO> users;

        if (search != null && !search.trim().isEmpty()) {
            users = userService.searchUsers(search.trim());
        } else {
            users = userService.getAllUsers();
        }

        req.setAttribute("users", users);
        req.getRequestDispatcher("/dashboard/admin/user-management.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        if ("create".equals(action)) {
            String username = req.getParameter("username");
            String role = req.getParameter("role");
            String password = req.getParameter("password");

            UserDTO dto = new UserDTO();
            dto.setUsername(username);
            dto.setRole(role);

            userService.createUser(dto, password);

        } else if ("update".equals(action)) {
            int id = Integer.parseInt(req.getParameter("userId"));
            String username = req.getParameter("username");

            UserDTO dto = new UserDTO();
            dto.setUserId(id);
            dto.setUsername(username);

            userService.updateUser(dto);

        } else if ("delete".equals(action)) {
            int id = Integer.parseInt(req.getParameter("userId"));
            userService.deleteUser(id);
        }

        resp.sendRedirect(req.getContextPath() + "/dashboard/admin/user-management");
    }
}