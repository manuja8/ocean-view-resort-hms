package com.oceanview.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;


@WebFilter({"/dashboard/*"})
public class AuthFilter implements Filter {

    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("loggedUser") == null) {
            res.sendRedirect(req.getContextPath() + "/auth/login.jsp");
            return;
        }

        // RBAC Protection
        String role = (String) session.getAttribute("role");
        String uri = req.getRequestURI();

        // protect admin area
        if (uri.contains("/dashboard/admin/") && !"ADMIN".equalsIgnoreCase(role)) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        chain.doFilter(request, response);
    }
}

/*
@WebFilter({"/dashboard/*"})  // <- protects all files inside dashboard folder
public class AuthFilter implements Filter {

    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("loggedUser") == null) {
            res.sendRedirect(req.getContextPath() + "/auth/login.jsp"); // safer redirect
            return;
        }

        chain.doFilter(request, response);
    }
}
*/
