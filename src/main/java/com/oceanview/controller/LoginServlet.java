package com.oceanview.controller;

import com.oceanview.entity.User;
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

        User user = authService.login(username, password);

        if (user != null) {

            HttpSession session = req.getSession();
            session.setAttribute("username", user.getUsername());
            session.setAttribute("role", user.getRoleName());
            session.setAttribute("userId", user.getUserId());

            res.sendRedirect("dashboard/dashboard.jsp");

        } else {
            res.sendRedirect("auth/login.jsp?error=1");
        }
    }
}
