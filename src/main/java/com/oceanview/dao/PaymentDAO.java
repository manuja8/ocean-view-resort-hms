package com.oceanview.dao;

import com.oceanview.dto.PaymentDTO;
import com.oceanview.entity.Payment;

import java.util.List;

public interface PaymentDAO {
    int save(Payment payment);

    List<PaymentDTO> findAll(String q, Integer statusId);

    double sumCompletedPaymentsForBill(int billId); // status_name = 'completed'
}