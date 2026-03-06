package com.oceanview.factory;

import com.oceanview.entity.Bill;
import com.oceanview.entity.Reservation;

public class BillFactory {

    public static Bill create(Reservation reservation, int createdByUserId) {
        Bill b = new Bill();
        b.setReservation(reservation);
        b.setCreatedByUserId(createdByUserId);
        b.setDiscount(0);
        b.setTax(0);
        b.setCanceled(false);
        return b;
    }
}