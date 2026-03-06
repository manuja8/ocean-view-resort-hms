package com.oceanview.factory;

import com.oceanview.dto.PaymentDTO;
import com.oceanview.entity.Payment;

import java.time.LocalDateTime;

public class PaymentFactory {

    public static Payment create(PaymentDTO dto, int createdByUserId) {
        Payment p = new Payment();
        p.setBillId(dto.getBillId());
        p.setAmount(dto.getAmount());
        p.setPaymentMethodId(dto.getPaymentMethodId());
        p.setPaymentStatusId(dto.getPaymentStatusId());

        String ref = dto.getPaymentReference();
        p.setPaymentReference((ref == null || ref.trim().isEmpty()) ? null : ref.trim());

        p.setPaymentDate(LocalDateTime.now());
        p.setCreatedByUserId(createdByUserId);
        return p;
    }
}