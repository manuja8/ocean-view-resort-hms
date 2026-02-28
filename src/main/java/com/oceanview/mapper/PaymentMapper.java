package com.oceanview.mapper;

import com.oceanview.dto.PaymentDTO;
import com.oceanview.entity.Payment;

public class PaymentMapper {

    public static PaymentDTO toDTO(Payment payment) {
        if (payment == null) return null;

        PaymentDTO dto = new PaymentDTO();
        dto.setPaymentId(payment.getPaymentId());
        dto.setBill(BillMapper.toDTO(payment.getBill()));
        dto.setAmount(payment.getAmount());
        dto.setPaymentMethodId(payment.getPaymentMethodId());
        dto.setPaymentStatusId(payment.getPaymentStatusId());
        dto.setPaymentReference(payment.getPaymentReference());
        dto.setPaymentDate(payment.getPaymentDate());

        return dto;
    }

    public static Payment toEntity(PaymentDTO dto) {
        if (dto == null) return null;

        Payment payment = new Payment();
        payment.setPaymentId(dto.getPaymentId());
        payment.setBill(BillMapper.toEntity(dto.getBill()));
        payment.setAmount(dto.getAmount());
        payment.setPaymentMethodId(dto.getPaymentMethodId());
        payment.setPaymentStatusId(dto.getPaymentStatusId());
        payment.setPaymentReference(dto.getPaymentReference());
        payment.setPaymentDate(dto.getPaymentDate());

        return payment;
    }
}