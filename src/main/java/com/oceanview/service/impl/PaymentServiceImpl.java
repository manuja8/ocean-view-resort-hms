package com.oceanview.service.impl;

import com.oceanview.dao.BillDAO;
import com.oceanview.dao.PaymentDAO;
import com.oceanview.dao.PaymentMethodDAO;
import com.oceanview.dao.PaymentStatusDAO;
import com.oceanview.dao.impl.BillDAOImpl;
import com.oceanview.dao.impl.PaymentDAOImpl;
import com.oceanview.dao.impl.PaymentMethodDAOImpl;
import com.oceanview.dao.impl.PaymentStatusDAOImpl;
import com.oceanview.observer.NotificationCenter;
import com.oceanview.observer.event.PaymentEvent;
import com.oceanview.dto.BillDTO;
import com.oceanview.dto.PaymentDTO;
import com.oceanview.dto.PaymentMethodDTO;
import com.oceanview.dto.PaymentStatusDTO;
import com.oceanview.entity.Bill;
import com.oceanview.entity.Payment;
import com.oceanview.factory.PaymentFactory;
import com.oceanview.service.BillingService;
import com.oceanview.service.PaymentService;
import com.oceanview.service.impl.BillingServiceImpl;

import java.util.List;

public class PaymentServiceImpl implements PaymentService {

    private final PaymentDAO paymentDAO = new PaymentDAOImpl();
    private final PaymentMethodDAO methodDAO = new PaymentMethodDAOImpl();
    private final PaymentStatusDAO statusDAO = new PaymentStatusDAOImpl();
    private final BillingService billingService = new BillingServiceImpl(); // uses your new Billing module
    private final BillDAO billDAO = new BillDAOImpl();

    @Override
    public List<PaymentDTO> list(String q, Integer statusId) {
        return paymentDAO.findAll(q, statusId);
    }

    @Override
    public List<PaymentMethodDTO> methods() {
        return methodDAO.findAll();
    }

    @Override
    public List<PaymentStatusDTO> statuses() {
        return statusDAO.findAll();
    }

    @Override
    public int processPayment(PaymentDTO dto, int userId) {

        if (dto.getBillId() <= 0) throw new IllegalArgumentException("Bill ID is required.");
        if (dto.getAmount() <= 0) throw new IllegalArgumentException("Amount must be greater than 0.");
        if (dto.getPaymentMethodId() <= 0) throw new IllegalArgumentException("Payment method is required.");
        if (dto.getPaymentStatusId() <= 0) throw new IllegalArgumentException("Payment status is required.");


        Bill bill = billDAO.findById(dto.getBillId());
        if (bill == null) throw new IllegalArgumentException("Bill not found.");
        if (bill.isCanceled()) throw new IllegalArgumentException("This bill is cancelled.");


        double alreadyPaid = paymentDAO.sumCompletedPaymentsForBill(dto.getBillId());
        double remaining = bill.getTotalAmount() - alreadyPaid;
        if (remaining < 0) remaining = 0;


        if (dto.getAmount() > remaining + 0.0001) {
            throw new IllegalArgumentException("Payment exceeds remaining amount. Remaining: " + String.format("%.2f", remaining));
        }

        Payment p = PaymentFactory.create(dto, userId);

        int paymentId = paymentDAO.save(p);

        String methodName = methods().stream()
                .filter(m -> m.getId() == dto.getPaymentMethodId())
                .map(m -> m.getName())
                .findFirst().orElse("Method#" + dto.getPaymentMethodId());

        String statusName = statuses().stream()
                .filter(s -> s.getId() == dto.getPaymentStatusId())
                .map(s -> s.getName())
                .findFirst().orElse("Status#" + dto.getPaymentStatusId());

        // Observer notify
        NotificationCenter.getInstance()
                .paymentSubject()
                .notifyObservers(new PaymentEvent(paymentId, dto.getBillId(), dto.getAmount(), methodName, statusName));


        return paymentId;
    }

    @Override
    public double paidAmountCompleted(int billId) {
        return paymentDAO.sumCompletedPaymentsForBill(billId);
    }
}