package com.sifthub.notification_service.strategy;

public class IVRNotificationStrategy implements NotificationStrategy {
    @Override
    public boolean send(String userId, String content) {
        // 1. Get user preferences using the userId
        // UserPreferences preferences = userPreferenceService.getPreferences(userId);
        // 2. Send IVR
        // thirdPartyIVRService.sendIVR(preferences.getPhoneNumber(), content);

        // For now, we'll just simulate sending IVR
        return true;
    }
}