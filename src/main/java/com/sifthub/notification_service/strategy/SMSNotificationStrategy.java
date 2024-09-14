package com.sifthub.notification_service.strategy;

public class SMSNotificationStrategy implements NotificationStrategy {
    @Override
    public boolean send(String userId, String content) {
        // 1. Get user preferences using the userId
        // UserPreferences preferences = userPreferenceService.getPreferences(userId);
        // 2. Send SMS
        // thirdPartySMSService.sendSMS(preferences.getPhoneNumber(), content);

        // For now, we'll just simulate sending SMS
        return true;
    }
}