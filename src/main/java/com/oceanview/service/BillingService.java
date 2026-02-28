package com.oceanview.service;

import com.oceanview.dto.BillDTO;

public interface BillingService {

    BillDTO generateBill(int reservationNo);

    BillDTO getBill(int reservationNo);
}