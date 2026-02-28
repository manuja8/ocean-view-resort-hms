package com.oceanview.controller;

import com.oceanview.dto.BillDTO;
import com.oceanview.service.BillingService;
import com.oceanview.service.impl.BillingServiceImpl;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.IOException;

@WebServlet("/billing")
public class BillingServlet extends HttpServlet {

    private BillingService billingService = new BillingServiceImpl();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("view".equals(action)) {
            int reservationNo = Integer.parseInt(request.getParameter("reservationNo"));
            BillDTO bill = billingService.getBill(reservationNo);
            request.setAttribute("bill", bill);
            request.getRequestDispatcher("/views/billing/view-bill.jsp")
                    .forward(request, response);
        } else {
            request.getRequestDispatcher("/views/billing/generate-bill.jsp")
                    .forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int reservationNo = Integer.parseInt(request.getParameter("reservationNo"));

        BillDTO bill = billingService.generateBill(reservationNo);

        request.setAttribute("bill", bill);
        request.getRequestDispatcher("/views/billing/view-bill.jsp")
                .forward(request, response);
    }
}