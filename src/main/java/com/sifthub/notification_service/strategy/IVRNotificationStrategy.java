package com.sifthub.notification_service.strategy;

public class IVRNotificationStrategy implements NotificationStrategy {
    @Override
    public boolean send(String content) {
        // Simulate sending IVR
        return true;
    }
}