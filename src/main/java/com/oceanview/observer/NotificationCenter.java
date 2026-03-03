package com.oceanview.observer;

import com.oceanview.observer.event.ComplaintEvent;
import com.oceanview.observer.event.PaymentEvent;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class NotificationCenter {

    private static final NotificationCenter instance = new NotificationCenter();

    private final Subject<ComplaintEvent> complaintSubject = new Subject<>();
    private final Subject<PaymentEvent> paymentSubject = new Subject<>();

    // In-memory store (good for assignment demo; resets on server restart)
    private final LinkedList<NotificationMessage> store = new LinkedList<>();
    private static final int MAX = 50;

    private NotificationCenter() {

        // Complaint observers
        complaintSubject.register(event -> add("admin",
                "New Complaint #" + event.getComplaintId(),
                "Guest ID: " + event.getGuestId() + " | Priority: " + event.getPriority() + " | " + event.getSubject()
        ));

        complaintSubject.register(event -> add("staff",
                "Complaint Received #" + event.getComplaintId(),
                "(" + event.getPriority() + ") " + event.getSubject()
        ));

        // Payment observers
        paymentSubject.register(event -> add("admin",
                "Payment #" + event.getPaymentId(),
                "Bill #" + event.getBillId() + " | " + money(event.getAmount()) +
                        " | " + event.getMethod() + " | " + event.getStatus()
        ));

        paymentSubject.register(event -> add("staff",
                "Payment Recorded #" + event.getPaymentId(),
                "Bill #" + event.getBillId() + " | " + money(event.getAmount()) +
                        " | " + event.getMethod() + " | " + event.getStatus()
        ));
    }

    public static NotificationCenter getInstance() {
        return instance;
    }

    public Subject<ComplaintEvent> complaintSubject() {
        return complaintSubject;
    }

    public Subject<PaymentEvent> paymentSubject() {
        return paymentSubject;
    }

    public List<NotificationMessage> getAll() {
        return Collections.unmodifiableList(store);
    }

    public List<NotificationMessage> getForAudience(String audience) {
        LinkedList<NotificationMessage> out = new LinkedList<>();
        for (NotificationMessage m : store) {
            if (m.getAudience().equalsIgnoreCase(audience)) out.add(m);
        }
        return out;
    }

    private synchronized void add(String audience, String title, String body) {
        store.addFirst(new NotificationMessage(audience, title, body, LocalDateTime.now()));
        while (store.size() > MAX) store.removeLast();
    }

    private String money(double amount) {
        return "LKR " + String.format("%.2f", amount);
    }
}