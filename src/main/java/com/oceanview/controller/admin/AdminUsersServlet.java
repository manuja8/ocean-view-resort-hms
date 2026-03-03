package com.oceanview.controller.admin;

import com.oceanview.dao.UserDAO;
import com.oceanview.dao.UserRoleDAO;
import com.oceanview.dao.impl.UserDAOImpl;
import com.oceanview.dao.impl.UserRoleDAOImpl;
import com.oceanview.dto.UserRoleDTO;
import com.oceanview.entity.User;
import com.oceanview.factory.UserFactory;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/admin/users")
public class AdminUsersServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAOImpl();
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
            }
            req.setAttribute("user", u);
            req.getRequestDispatcher("/WEB-INF/views/admin/users/form.jsp").forward(req, resp);
            return;
        }

        if ("edit".equalsIgnoreCase(mode)) {
            int id = Integer.parseInt(req.getParameter("id"));
            User u = userDAO.findById(id);
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
        req.setAttribute("users", (q == null || q.trim().isEmpty()) ? userDAO.findAll() : userDAO.search(q));
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
                userDAO.delete(id);
                req.getSession().setAttribute("flashSuccess", "User deleted.");
                resp.sendRedirect(req.getContextPath() + "/admin/users");
                return;
            }

            int roleId = Integer.parseInt(req.getParameter("roleId"));
            UserRoleDTO role = roleDAO.findById(roleId);
            if (role == null) throw new IllegalArgumentException("Invalid role");

            User u = UserFactory.createUser(role.getRoleName());
            if (u == null) throw new IllegalArgumentException("Invalid role");

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
                u.setUpdatedByUserId(adminUserId);

                // password optional on edit
                if (plainPassword != null && !plainPassword.trim().isEmpty()) {
                    u.setPasswordHash(BCrypt.hashpw(plainPassword, BCrypt.gensalt()));
                } else {
                    u.setPasswordHash(null); // DAO keeps old password_hash
                }

                userDAO.update(u);
                req.getSession().setAttribute("flashSuccess", "User updated.");
            } else {
                // create requires password
                if (plainPassword == null || plainPassword.trim().isEmpty()) {
                    throw new IllegalArgumentException("Password is required to create a user.");
                }
                u.setPasswordHash(BCrypt.hashpw(plainPassword, BCrypt.gensalt()));
                u.setCreatedByUserId(adminUserId);

                userDAO.save(u);
                req.getSession().setAttribute("flashSuccess", "User created.");
            }

            resp.sendRedirect(req.getContextPath() + "/admin/users");

        } catch (Exception ex) {
            req.setAttribute("error", ex.getMessage());
            req.setAttribute("roles", roleDAO.findAll());
            req.setAttribute("user", userFromRequest(req));
            req.setAttribute("error", message(ex));
            req.setAttribute("mode", mode.isBlank() ? "create" : mode);
            if (req.getParameter("expiryDate") != null)
                req.setAttribute("expiryDateValue", req.getParameter("expiryDate"));
            req.getRequestDispatcher("/WEB-INF/views/admin/users/form.jsp").forward(req, resp);
            return;

        }
    }

    private String message(Exception ex) {
        Throwable t = ex;
        while (t.getCause() != null) t = t.getCause();

        String msg = t.getMessage() == null ? ex.getMessage() : t.getMessage();
        if (msg == null) return "Operation failed.";

        if (msg.contains("Duplicate entry") || msg.contains("1062")) {
            String lower = msg.toLowerCase();
            if (lower.contains("username")) return "Username already exists. Please choose another one.";
            if (lower.contains("contact_no") || lower.contains("contact"))
                return "Contact number already exists. Please use another.";
            return "Duplicate value detected. Please check again.";
        }

        return msg;
    }

    private User userFromRequest(HttpServletRequest req) {
        // fallback just to re-fill inputs
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