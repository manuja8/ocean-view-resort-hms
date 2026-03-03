package com.oceanview.controller.admin;

import com.oceanview.dao.UserRoleDAO;
import com.oceanview.dao.impl.UserRoleDAOImpl;
import com.oceanview.dto.UserRoleDTO;
import com.oceanview.entity.User;
import com.oceanview.factory.UserFactory;
import com.oceanview.service.UserService;
import com.oceanview.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/admin/users")
public class AdminUsersServlet extends HttpServlet {

    private final UserService userService = new UserServiceImpl();
    private final UserRoleDAO roleDAO = new UserRoleDAOImpl();

    private static final DateTimeFormatter DT_LOCAL = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String mode = req.getParameter("mode");
        if (mode == null || mode.isBlank()) mode = "list";

        // flash messages
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
            req.setAttribute("roles", roleDAO.findAll());

            // empty user for form binding (default receptionist object)
            User u = UserFactory.createUser("receptionist");
            if (u != null) {
                u.setActive(true);
                u.setBlocked(false);
                u.setExpiryDate(LocalDateTime.now().plusYears(1));
                req.setAttribute("expiryDateValue", u.getExpiryDate().format(DT_LOCAL));
            }

            req.setAttribute("user", u);
            req.getRequestDispatcher("/WEB-INF/views/admin/users/form.jsp").forward(req, resp);
            return;
        }

        if ("edit".equalsIgnoreCase(mode)) {
            int id = Integer.parseInt(req.getParameter("id"));
            User u = userService.getById(id);

            if (u == null) {
                req.getSession().setAttribute("flashError", "User not found.");
                resp.sendRedirect(req.getContextPath() + "/admin/users");
                return;
            }

            req.setAttribute("roles", roleDAO.findAll());
            req.setAttribute("user", u);

            // format expiry for datetime-local
            if (u.getExpiryDate() != null) {
                req.setAttribute("expiryDateValue", u.getExpiryDate().format(DT_LOCAL));
            }

            req.getRequestDispatcher("/WEB-INF/views/admin/users/form.jsp").forward(req, resp);
            return;
        }

        // list
        String q = req.getParameter("q");
        req.setAttribute("users", userService.list(q));
        req.getRequestDispatcher("/WEB-INF/views/admin/users/list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        String mode = req.getParameter("mode");
        if (mode == null) mode = "";

        Integer adminUserId = (Integer) req.getSession().getAttribute("userId");
        if (adminUserId == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {

            if ("delete".equalsIgnoreCase(mode)) {
                int id = Integer.parseInt(req.getParameter("id"));
                userService.delete(id);
                req.getSession().setAttribute("flashSuccess", "User deleted.");
                resp.sendRedirect(req.getContextPath() + "/admin/users");
                return;
            }


            int roleId = Integer.parseInt(req.getParameter("roleId"));
            UserRoleDTO role = roleDAO.findById(roleId);
            if (role == null) throw new IllegalArgumentException("Invalid role");


            User u = UserFactory.createUser(role.getRoleName());
            if (u == null) throw new IllegalArgumentException("Invalid role");

            // set fields from request
            u.setUsername(req.getParameter("username"));
            u.setFullName(req.getParameter("fullName"));
            u.setAddress(req.getParameter("address"));
            u.setContactNo(req.getParameter("contactNo"));
            u.setRoleId(roleId);
            u.setRoleName(role.getRoleName());

            u.setActive("1".equals(req.getParameter("isActive")) || "true".equalsIgnoreCase(req.getParameter("isActive")));
            u.setBlocked("1".equals(req.getParameter("isBlocked")) || "true".equalsIgnoreCase(req.getParameter("isBlocked")));

            
            String expiryStr = req.getParameter("expiryDate");
            if (expiryStr == null || expiryStr.trim().isEmpty()) {
                u.setExpiryDate(LocalDateTime.now().plusYears(1));
            } else {
                u.setExpiryDate(LocalDateTime.parse(expiryStr.trim(), DT_LOCAL));
            }

            String plainPassword = req.getParameter("password");

            if ("edit".equalsIgnoreCase(mode)) {
                u.setUserId(Integer.parseInt(req.getParameter("id")));
                userService.update(u, adminUserId, plainPassword);
                req.getSession().setAttribute("flashSuccess", "User updated.");
            } else {
                userService.create(u, adminUserId, plainPassword);
                req.getSession().setAttribute("flashSuccess", "User created.");
            }

            resp.sendRedirect(req.getContextPath() + "/admin/users");

        } catch (Exception ex) {

            req.setAttribute("error", userService.friendlyMessage(ex));
            req.setAttribute("roles", roleDAO.findAll());
            req.setAttribute("user", userFromRequest(req));
            req.setAttribute("mode", mode.isBlank() ? "create" : mode);

            if (req.getParameter("expiryDate") != null)
                req.setAttribute("expiryDateValue", req.getParameter("expiryDate"));

            req.getRequestDispatcher("/WEB-INF/views/admin/users/form.jsp").forward(req, resp);
        }
    }

    private User userFromRequest(HttpServletRequest req) {


        User u = UserFactory.createUser("receptionist");
        if (u == null) return null;

        try {
            u.setUserId(Integer.parseInt(req.getParameter("id")));
        } catch (Exception ignored) {
        }

        u.setUsername(req.getParameter("username"));
        u.setFullName(req.getParameter("fullName"));
        u.setAddress(req.getParameter("address"));
        u.setContactNo(req.getParameter("contactNo"));

        try {
            u.setRoleId(Integer.parseInt(req.getParameter("roleId")));
        } catch (Exception ignored) {
        }

        u.setActive("1".equals(req.getParameter("isActive")) || "true".equalsIgnoreCase(req.getParameter("isActive")));
        u.setBlocked("1".equals(req.getParameter("isBlocked")) || "true".equalsIgnoreCase(req.getParameter("isBlocked")));

        try {
            String expiryStr = req.getParameter("expiryDate");
            if (expiryStr != null && !expiryStr.trim().isEmpty()) {
                u.setExpiryDate(LocalDateTime.parse(expiryStr.trim(), DT_LOCAL));
            }
        } catch (Exception ignored) {
        }

        return u;
    }
}