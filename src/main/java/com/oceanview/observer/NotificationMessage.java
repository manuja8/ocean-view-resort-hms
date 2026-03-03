package com.oceanview.observer;

import java.time.LocalDateTime;

public class NotificationMessage {
    private final String audience; // "admin" or "staff"
    private final String title;
    private final String body;
    private final LocalDateTime createdAt;

    public NotificationMessage(String audience, String title, String body, LocalDateTime createdAt) {
        this.audience = audience;
        this.title = title;
        this.body = body;
        this.createdAt = createdAt;
    }

    public String getAudience() {
        return audience;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}