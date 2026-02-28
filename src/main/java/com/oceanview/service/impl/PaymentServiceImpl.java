package com.oceanview.service.impl;

import com.oceanview.dao.BillDAO;
import com.oceanview.dao.PaymentDAO;
import com.oceanview.dao.impl.BillDAOImpl;
import com.oceanview.dao.impl.PaymentDAOImpl;
import com.oceanview.entity.Bill;
import com.oceanview.entity.Payment;
import com.oceanview.service.PaymentService;

import java.time.LocalDateTime;

public class PaymentServiceImpl implements PaymentService {

    private PaymentDAO paymentDAO = new PaymentDAOImpl();
    private BillDAO billDAO = new BillDAOImpl();

    @Override
    public boolean processFullPayment(int billId, int methodId, String reference) {

        if (paymentDAO.existsByBillId(billId)) {
            return false; // already paid
        }

        Bill bill = billDAO.findById(billId);
        if (bill == null) return false;

        Payment payment = new Payment();
        payment.setBill(bill);
        payment.setAmount(bill.getTotalAmount());
        payment.setPaymentMethodId(methodId);
        payment.setPaymentStatusId(2); // assuming 2 = COMPLETED
        payment.setPaymentReference(reference);
        payment.setPaymentDate(LocalDateTime.now());

        paymentDAO.save(payment);

        return true;
    }
}