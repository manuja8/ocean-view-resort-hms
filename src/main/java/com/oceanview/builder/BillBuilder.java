package com.oceanview.builder;

import com.oceanview.entity.Bill;
import com.oceanview.entity.Reservation;

public class BillBuilder {

    private final Bill bill;
    private final StringBuilder charges = new StringBuilder();
    private double runningTotal = 0;

    public BillBuilder(Reservation reservation) {
        bill = new Bill();
        bill.setReservation(reservation);
    }

    public BillBuilder addRoomCharge() {
        long nights = bill.getReservation().getCheckOut().toEpochDay()
                - bill.getReservation().getCheckIn().toEpochDay();

        if (nights <= 0) throw new IllegalArgumentException("Invalid stay dates (nights must be >= 1)");

        bill.setNumNights((int) nights);

        double rate = bill.getReservation().getRoomTypePrice();
        double amount = nights * rate;

        charges.append("Room Charge (")
                .append(nights).append(" nights × ").append(rate)
                .append("): ").append(amount).append("\n");

        runningTotal += amount;
        return this;
    }

    public BillBuilder applyDiscount(double discountAmount) {
        if (discountAmount < 0) throw new IllegalArgumentException("Discount cannot be negative");
        if (discountAmount > runningTotal) throw new IllegalArgumentException("Discount cannot exceed subtotal");

        bill.setDiscount(discountAmount);

        if (discountAmount > 0) {
            charges.append("Discount: -").append(discountAmount).append("\n");
            runningTotal -= discountAmount;
        }
        return this;
    }

    public BillBuilder applyTaxPercent(double percent) {
        if (percent < 0) throw new IllegalArgumentException("Tax percent cannot be negative");

        double taxAmount = runningTotal * percent / 100.0;
        bill.setTax(taxAmount);

        if (taxAmount > 0) {
            charges.append("Tax (").append(percent).append("%): ").append(taxAmount).append("\n");
            runningTotal += taxAmount;
        }
        return this;
    }

   
    public BillBuilder applyTaxAmount(double taxAmount) {
        if (taxAmount < 0) throw new IllegalArgumentException("Tax cannot be negative");
        bill.setTax(taxAmount);

        if (taxAmount > 0) {
            charges.append("Tax: ").append(taxAmount).append("\n");
            runningTotal += taxAmount;
        }
        return this;
    }

    public Bill build() {
        bill.setTotalAmount(runningTotal);
        bill.setItemizedCharges(charges.toString());
        return bill;
    }
}