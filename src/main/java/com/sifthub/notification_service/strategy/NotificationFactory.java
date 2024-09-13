package com.sifthub.notification_service.strategy;

import com.sifthub.notification_service.model.NotificationType;

public class NotificationFactory {
    public static NotificationStrategy getStrategy(NotificationType type) {
        return switch (type) {
            case EMAIL -> new EmailNotificationStrategy();
            case SMS -> new SMSNotificationStrategy();
            case IVR -> new IVRNotificationStrategy();
            default -> throw new IllegalArgumentException("Unsupported notification type: " + type);
        };
    }
}