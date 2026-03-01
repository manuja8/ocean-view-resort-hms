package com.oceanview.controller;

import com.oceanview.dto.ReportDTO;
import com.oceanview.facade.ReportFacade;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/dashboard/admin/report")
public class ReportServlet extends HttpServlet {

    private ReportFacade reportFacade = new ReportFacade();

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        ReportDTO report = reportFacade.generateReport();

        req.setAttribute("report", report);

        // Forward to dashboard/admin/report.jsp
        req.getRequestDispatcher("/dashboard/admin/report.jsp").forward(req, res);
    }
}