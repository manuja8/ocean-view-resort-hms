package com.oceanview.service;

import com.oceanview.dto.BillDTO;

public interface BillingService {


    BillDTO generateBill(int reservationNo, double discountAmount, double taxPercent, int createdByUserId, boolean forceRecalculate);

    BillDTO getBill(int reservationNo);

    BillDTO getBillById(int billId);
}