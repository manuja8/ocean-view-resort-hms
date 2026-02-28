package com.oceanview.dao;

import com.oceanview.entity.Bill;

public interface BillDAO {

    int save(Bill bill);

    Bill findByReservation(int reservationNo);

    Bill findById(int billId);
}