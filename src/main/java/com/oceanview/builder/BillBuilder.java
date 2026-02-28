package com.oceanview.builder;

import com.oceanview.entity.Bill;
import com.oceanview.entity.Reservation;

public class BillBuilder {

    private Bill bill;
    private StringBuilder charges = new StringBuilder();
    private double total = 0;

    public BillBuilder(Reservation reservation) {
        bill = new Bill();
        bill.setReservation(reservation);
    }

    public BillBuilder addRoomCharge() {
        long nights = bill.getReservation().getCheckOut().toEpochDay()
                - bill.getReservation().getCheckIn().toEpochDay();

        double rate = bill.getReservation().getRoom().getRate();
        double amount = nights * rate;

        charges.append("Room Charge (" + nights + " nights): ").append(amount).append("\n");
        total += amount;

        return this;
    }

    public BillBuilder addServiceCharge(double amount) {
        charges.append("Service Charge: ").append(amount).append("\n");
        total += amount;
        return this;
    }

    public BillBuilder addTax(double percentage) {
        double taxAmount = total * percentage / 100;
        charges.append("Tax (" + percentage + "%): ").append(taxAmount).append("\n");
        total += taxAmount;
        return this;
    }

    public Bill build() {
        bill.setTotalAmount(total);
        bill.setItemizedCharges(charges.toString());
        return bill;
    }
}