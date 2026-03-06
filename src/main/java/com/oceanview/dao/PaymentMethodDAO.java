package com.oceanview.dao;

import com.oceanview.dto.PaymentMethodDTO;

import java.util.List;

public interface PaymentMethodDAO {
    List<PaymentMethodDTO> findAll();
}