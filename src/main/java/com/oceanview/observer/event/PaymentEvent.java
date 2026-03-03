package com.oceanview.observer.event;

public class PaymentEvent {

    private final int paymentId;
    private final int billId;
    private final double amount;
    private final String method;
    private final String status;

    public PaymentEvent(int paymentId, int billId, double amount, String method, String status) {
        this.paymentId = paymentId;
        this.billId = billId;
        this.amount = amount;
        this.method = method;
        this.status = status;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public int getBillId() {
        return billId;
    }

    public double getAmount() {
        return amount;
    }

    public String getMethod() {
        return method;
    }

    public String getStatus() {
        return status;
    }
}