package com.oceanview.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;

@WebFilter("/dashboard/*")
public class RoleFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        // Cast the generic request and response to HTTP-specific objects
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Get the session from the request, if it exists
        HttpSession session = req.getSession(false);

        // If there's no session, redirect the user to the login page
        if (session == null) {
            res.sendRedirect(req.getContextPath() + "/auth/login.jsp");
            return;
        }

        // Retrieve the user's role from the session
        String role = (String) session.getAttribute("role");
        String uri = req.getRequestURI();

        // If there's no role in the session, redirect to login
        if (role == null) {
            res.sendRedirect(req.getContextPath() + "/auth/login.jsp");
            return;
        }

        //ADMIN ONLY access
        if (uri.equals("/dashboard/admin/users") || uri.equals("/dashboard/admin/report")) {
            // Only allow 'admin' users to access these pages
            if (!role.equalsIgnoreCase("admin")) {
                res.sendRedirect(req.getContextPath() + "/dashboard/dashboard.jsp");
                return;
            }
        }

        // RECEPTIONIST BLOCKED AREAS
        if (role.equalsIgnoreCase("receptionist")) {
            // If the URL contains '/admin/', block access for receptionists
            if (uri.contains("/admin/")) {
                res.sendRedirect(req.getContextPath() + "/dashboard/dashboard.jsp");
                return;
            }
        }

        // Allow the request to proceed further if all checks pass
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Filter initialization, if necessary
    }

    @Override
    public void destroy() {
        // Clean up resources, if necessary
    }
}