package com.oceanview.controller.staff;

import com.oceanview.dto.PaymentDTO;
import com.oceanview.service.PaymentService;
import com.oceanview.service.impl.PaymentServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/staff/payments")
public class StaffPaymentsServlet extends HttpServlet {

    private final PaymentService paymentService = new PaymentServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String mode = req.getParameter("mode");
        if (mode == null || mode.isBlank()) mode = "list";

        // flash
        if (req.getSession().getAttribute("flashError") != null) {
            req.setAttribute("error", req.getSession().getAttribute("flashError"));
            req.getSession().removeAttribute("flashError");
        }
        if (req.getSession().getAttribute("flashSuccess") != null) {
            req.setAttribute("success", req.getSession().getAttribute("flashSuccess"));
            req.getSession().removeAttribute("flashSuccess");
        }

        if ("create".equalsIgnoreCase(mode)) {
            req.setAttribute("methods", paymentService.methods());
            req.setAttribute("statuses", paymentService.statuses());

            PaymentDTO dto = new PaymentDTO();
            try {
                dto.setBillId(Integer.parseInt(req.getParameter("billId")));
            } catch (Exception ignored) {
            }
            req.setAttribute("payment", dto);

            // show remaining amount if billId given
            if (dto.getBillId() > 0) {
                req.setAttribute("paidCompleted", paymentService.paidAmountCompleted(dto.getBillId()));
            }

            req.getRequestDispatcher("/WEB-INF/views/staff/payments/form.jsp").forward(req, resp);
            return;
        }

        // list
        String q = req.getParameter("q");
        Integer statusId = null;
        try {
            statusId = Integer.parseInt(req.getParameter("statusId"));
        } catch (Exception ignored) {
        }

        req.setAttribute("statuses", paymentService.statuses());
        req.setAttribute("payments", paymentService.list(q, statusId));
        req.getRequestDispatcher("/WEB-INF/views/staff/payments/list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Integer userId = (Integer) req.getSession().getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            PaymentDTO dto = new PaymentDTO();
            dto.setBillId(Integer.parseInt(req.getParameter("billId")));
            dto.setAmount(Double.parseDouble(req.getParameter("amount")));
            dto.setPaymentMethodId(Integer.parseInt(req.getParameter("methodId")));
            dto.setPaymentStatusId(Integer.parseInt(req.getParameter("statusId")));
            dto.setPaymentReference(req.getParameter("reference"));

            paymentService.processPayment(dto, userId);

            req.getSession().setAttribute("flashSuccess", "Payment processed successfully.");
            resp.sendRedirect(req.getContextPath() + "/staff/payments");
        } catch (Exception ex) {
            req.getSession().setAttribute("flashError", ex.getMessage());
            resp.sendRedirect(req.getContextPath() + "/staff/payments?mode=create&billId=" + req.getParameter("billId"));
        }
    }
}