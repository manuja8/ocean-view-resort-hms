package com.oceanview.dao;

import com.oceanview.entity.Payment;

public interface PaymentDAO {

    void save(Payment payment);

    boolean existsByBillId(int billId);
}