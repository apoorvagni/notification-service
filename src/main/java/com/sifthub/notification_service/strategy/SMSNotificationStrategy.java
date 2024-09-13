package com.sifthub.notification_service.strategy;

public class SMSNotificationStrategy implements NotificationStrategy {
    @Override
    public boolean send(String content) {
        // Simulate sending SMS
        return true;
    }
}