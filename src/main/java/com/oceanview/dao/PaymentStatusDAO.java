package com.oceanview.dao;

import com.oceanview.dto.PaymentStatusDTO;

import java.util.List;

public interface PaymentStatusDAO {
    List<PaymentStatusDTO> findAll();
}