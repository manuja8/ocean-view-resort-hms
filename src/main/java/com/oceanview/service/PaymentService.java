package com.oceanview.service;

public interface PaymentService {

    boolean processFullPayment(int billId, int methodId, String reference);
}