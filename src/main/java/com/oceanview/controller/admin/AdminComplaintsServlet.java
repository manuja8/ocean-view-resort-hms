package com.oceanview.controller.admin;

import com.oceanview.service.ComplaintService;
import com.oceanview.service.impl.ComplaintServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/complaints")
public class AdminComplaintsServlet extends HttpServlet {

    private final ComplaintService complaintService = new ComplaintServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        String mode = req.getParameter("mode");
        if (!"delete".equalsIgnoreCase(mode)) {
            resp.sendRedirect(req.getContextPath() + "/staff/complaints");
            return;
        }

        try {
            int id = Integer.parseInt(req.getParameter("id"));
            complaintService.delete(id);
            req.getSession().setAttribute("flashSuccess", "Complaint deleted.");
        } catch (Exception ex) {
            req.getSession().setAttribute("flashError", ex.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/staff/complaints");
    }
}