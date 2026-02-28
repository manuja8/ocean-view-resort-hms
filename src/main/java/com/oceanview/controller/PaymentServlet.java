package com.oceanview.controller;

import com.oceanview.service.PaymentService;
import com.oceanview.service.impl.PaymentServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/payment")
public class PaymentServlet extends HttpServlet {

    private PaymentService paymentService = new PaymentServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int billId = Integer.parseInt(request.getParameter("billId"));
        int methodId = Integer.parseInt(request.getParameter("methodId"));
        String reference = request.getParameter("reference");

        boolean success = paymentService.processFullPayment(billId, methodId, reference);

        if (success) {
            response.sendRedirect("views/payment/payment-success.jsp");
        } else {
            request.setAttribute("error", "Payment already exists or invalid bill.");
            request.getRequestDispatcher("views/payment/payment-form.jsp")
                    .forward(request, response);
        }
    }
}