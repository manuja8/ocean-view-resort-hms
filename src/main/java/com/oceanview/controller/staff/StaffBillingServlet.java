package com.oceanview.controller.staff;

import com.oceanview.dto.BillDTO;
import com.oceanview.service.BillingService;
import com.oceanview.service.PaymentService;
import com.oceanview.service.impl.BillingServiceImpl;
import com.oceanview.service.impl.PaymentServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/staff/billing")
public class StaffBillingServlet extends HttpServlet {

    private final BillingService billingService = new BillingServiceImpl();
    private final PaymentService paymentService = new PaymentServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String mode = req.getParameter("mode");
        if (mode == null || mode.isBlank()) mode = "generate";

        // flash
        if (req.getSession().getAttribute("flashError") != null) {
            req.setAttribute("error", req.getSession().getAttribute("flashError"));
            req.getSession().removeAttribute("flashError");
        }
        if (req.getSession().getAttribute("flashSuccess") != null) {
            req.setAttribute("success", req.getSession().getAttribute("flashSuccess"));
            req.getSession().removeAttribute("flashSuccess");
        }

        if ("view".equalsIgnoreCase(mode)) {
            int reservationId = Integer.parseInt(req.getParameter("reservationId"));
            BillDTO bill = billingService.getBill(reservationId);
            req.setAttribute("bill", bill);

            if (bill != null) {
                double paid = paymentService.paidAmountCompleted(bill.getBillNo());
                double total = bill.getTotalAmount();
                double remaining = Math.max(0.0, total - paid);

                req.setAttribute("paidAmount", paid);
                req.setAttribute("remainingAmount", remaining);
                req.setAttribute("isPaid", remaining <= 0.0001);
            }

            req.getRequestDispatcher("/WEB-INF/views/staff/billing/view.jsp").forward(req, resp);
            return;
        }

        if ("print".equalsIgnoreCase(mode)) {
            int billId = Integer.parseInt(req.getParameter("billId"));
            Integer userId = (Integer) req.getSession().getAttribute("userId");
            if (userId == null) {
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }

            BillDTO bill = billingService.getBillById(billId);
            if (bill == null) {
                req.getSession().setAttribute("flashError", "Bill not found.");
                resp.sendRedirect(req.getContextPath() + "/staff/billing");
                return;
            }

            double paid = paymentService.paidAmountCompleted(billId);
            double total = bill.getTotalAmount();
            double remaining = Math.max(0.0, total - paid);

            req.setAttribute("bill", bill);
            req.setAttribute("paidAmount", paid);
            req.setAttribute("remainingAmount", remaining);
            req.setAttribute("isPaid", remaining <= 0.0001);

            // log print
            ((BillingServiceImpl) billingService).logPrint(billId, userId);

            req.getRequestDispatcher("/WEB-INF/views/staff/billing/print.jsp").forward(req, resp);
            return;
        }

        // default generate screen
        req.getRequestDispatcher("/WEB-INF/views/staff/billing/generate.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        Integer userId = (Integer) req.getSession().getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            int reservationId = Integer.parseInt(req.getParameter("reservationId"));

            double discount = parseDouble(req.getParameter("discount"));
            double taxPercent = parseDouble(req.getParameter("taxPercent"));

            boolean force = "on".equalsIgnoreCase(req.getParameter("force"));

            billingService.generateBill(reservationId, discount, taxPercent, userId, force);

            if (force) {
                req.getSession().setAttribute("flashSuccess", "Previous bill cancelled and new bill generated successfully.");
            } else {
                req.getSession().setAttribute("flashSuccess", "Bill calculated successfully.");
            }

            resp.sendRedirect(req.getContextPath() + "/staff/billing?mode=view&reservationId=" + reservationId);

        } catch (Exception ex) {
            req.getSession().setAttribute("flashError", ex.getMessage());
            resp.sendRedirect(req.getContextPath() + "/staff/billing");
        }
    }

    private double parseDouble(String v) {
        try {
            if (v == null || v.trim().isEmpty()) return 0.0;
            return Double.parseDouble(v.trim());
        } catch (Exception e) {
            return 0.0;
        }
    }
}