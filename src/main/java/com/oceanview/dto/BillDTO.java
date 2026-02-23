package com.oceanview.dto;

public class BillDTO {

    private int billNo;
    private ReservationDTO reservation;
    private double totalAmount;
    private String itemizedCharges;


    public int getBillNo() {
        return billNo;
    }

    public void setBillNo(int billNo) {
        this.billNo = billNo;
    }

    public ReservationDTO getReservation() {
        return reservation;
    }

    public void setReservation(ReservationDTO reservation) {
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