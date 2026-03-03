package com.oceanview.dao;

import com.oceanview.entity.Bill;

public interface BillDAO {

    int save(Bill bill);

    Bill findByReservation(int reservationNo); // reservation_id

    Bill findById(int billId);

    void cancel(int billId, int updatedByUserId);
}