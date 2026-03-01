package com.oceanview.dto;

public class ReportDTO {

    private int totalReservations;
    private int totalGuests;
    private int totalRooms;

    private int totalBills;
    private int totalPayments;

    private int paidBills;
    private int unpaidBills;

    private double totalRevenue;

    // Getters and Setters
    public int getTotalReservations() {
        return totalReservations;
    }

    public void setTotalReservations(int totalReservations) {
        this.totalReservations = totalReservations;
    }

    public int getTotalGuests() {
        return totalGuests;
    }

    public void setTotalGuests(int totalGuests) {
        this.totalGuests = totalGuests;
    }

    public int getTotalRooms() {
        return totalRooms;
    }

    public void setTotalRooms(int totalRooms) {
        this.totalRooms = totalRooms;
    }

    public int getTotalBills() {
        return totalBills;
    }

    public void setTotalBills(int totalBills) {
        this.totalBills = totalBills;
    }

    public int getTotalPayments() {
        return totalPayments;
    }

    public void setTotalPayments(int totalPayments) {
        this.totalPayments = totalPayments;
    }

    public int getPaidBills() {
        return paidBills;
    }

    public void setPaidBills(int paidBills) {
        this.paidBills = paidBills;
    }

    public int getUnpaidBills() {
        return unpaidBills;
    }

    public void setUnpaidBills(int unpaidBills) {
        this.unpaidBills = unpaidBills;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}