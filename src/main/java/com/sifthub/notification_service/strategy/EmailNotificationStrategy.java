package com.sifthub.notification_service.strategy;

public class EmailNotificationStrategy implements NotificationStrategy {
    @Override
    public boolean send(String content) {
        // Simulate sending email
        return true;
    }
}