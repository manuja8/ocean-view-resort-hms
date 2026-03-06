package com.oceanview.service;

import com.oceanview.dto.PaymentDTO;
import com.oceanview.dto.PaymentMethodDTO;
import com.oceanview.dto.PaymentStatusDTO;

import java.util.List;

public interface PaymentService {

    List<PaymentDTO> list(String q, Integer statusId);

    List<PaymentMethodDTO> methods();

    List<PaymentStatusDTO> statuses();


    int processPayment(PaymentDTO dto, int userId);

    double paidAmountCompleted(int billId);
}