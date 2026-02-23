
package com.oceanview.controller;

import com.oceanview.dto.UserDTO;
import com.oceanview.service.AuthService;
import com.oceanview.service.impl.AuthServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private AuthService authService = new AuthServiceImpl();

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        UserDTO user = authService.login(username, password);

        if (user != null) {

            // create session
            HttpSession session = req.getSession(true);

            session.setAttribute("loggedUser", user);
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRole());

            // session timeout (30 mins)
            session.setMaxInactiveInterval(30 * 60);

            res.sendRedirect("/dashboard/dashboard.jsp");

        } else {
            req.setAttribute("error", "Invalid username or password");
            req.getRequestDispatcher("/auth/login.jsp").forward(req, res);
        }
    }
}
