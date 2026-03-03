package com.oceanview.observer.event;

public class ComplaintEvent {
    private final int complaintId;
    private final int guestId;
    private final String subject;
    private final String priority;

    public ComplaintEvent(int complaintId, int guestId, String subject, String priority) {
        this.complaintId = complaintId;
        this.guestId = guestId;
        this.subject = subject;
        this.priority = priority;
    }

    public int getComplaintId() {
        return complaintId;
    }

    public int getGuestId() {
        return guestId;
    }

    public String getSubject() {
        return subject;
    }

    public String getPriority() {
        return priority;
    }
}