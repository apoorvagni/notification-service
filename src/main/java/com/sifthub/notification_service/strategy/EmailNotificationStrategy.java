package com.sifthub.notification_service.strategy;

public class EmailNotificationStrategy implements NotificationStrategy {
    @Override
    public boolean send(String userId, String content) {
        // 1. Get user preferences using the userId
        // UserPreferences preferences = userPreferenceService.getPreferences(userId);
        // 2. Send email
        // thirdPartyEmailService.sendEmail(preferences.getEmail(), content);

        // For now, we'll just simulate sending email
        return true;
    }
}