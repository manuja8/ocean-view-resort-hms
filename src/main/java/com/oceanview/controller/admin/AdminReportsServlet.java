package com.oceanview.controller.admin;

import com.oceanview.dto.ReportRowDTO;
import com.oceanview.facade.ReportFacade;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/admin/reports")
public class AdminReportsServlet extends HttpServlet {

    private final ReportFacade reportFacade = new ReportFacade();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String type = req.getParameter("type");
        String fromStr = req.getParameter("from");
        String toStr = req.getParameter("to");

        // First visit: don't generate yet (so the table shows "No report generated yet")
        boolean hasAnyParam = (type != null || fromStr != null || toStr != null);

        if (hasAnyParam) {
            LocalDate from = (fromStr == null || fromStr.isBlank()) ? null : LocalDate.parse(fromStr);
            LocalDate to = (toStr == null || toStr.isBlank()) ? null : LocalDate.parse(toStr);

            List<ReportRowDTO> rows = reportFacade.generateRows(type, from, to);
            req.setAttribute("reportRows", rows);
        }

        req.getRequestDispatcher("/WEB-INF/views/admin/reports/report.jsp").forward(req, resp);
    }
}