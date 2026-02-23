package com.oceanview.entity;

public class Bill {

    private int billNo;
    private Reservation reservation;
    private double totalAmount;
    private String itemizedCharges;

    public Bill() {}

    public int getBillNo() {
        return billNo;
    }

    public void setBillNo(int billNo) {
        this.billNo = billNo;
    }


    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }


    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }


    public String getItemizedCharges() {
        return itemizedCharges;
    }

    public void setItemizedCharges(String itemizedCharges) {
        this.itemizedCharges = itemizedCharges;
    }
}