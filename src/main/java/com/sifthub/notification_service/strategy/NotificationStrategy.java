package com.sifthub.notification_service.strategy;

public interface NotificationStrategy {
    boolean send(String content);
}