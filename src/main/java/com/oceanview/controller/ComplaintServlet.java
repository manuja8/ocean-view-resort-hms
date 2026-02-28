package com.oceanview.controller;

import com.oceanview.dto.ComplaintDTO;
import com.oceanview.dto.GuestDTO;
import com.oceanview.service.ComplaintService;
import com.oceanview.service.impl.ComplaintServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.IOException;

@WebServlet("/complaints")
public class ComplaintServlet extends HttpServlet {

    private ComplaintService service = new ComplaintServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ComplaintDTO dto = new ComplaintDTO();
        dto.setSubject(request.getParameter("subject"));
        dto.setDescription(request.getParameter("description"));
        dto.setStatus("open");
        dto.setPriority(request.getParameter("priority"));

        service.createComplaint(dto);

        response.sendRedirect("complaints");
    }
}