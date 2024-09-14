package com.sifthub.notification_service.repository;

import com.sifthub.notification_service.model.DailyStats;
import com.sifthub.notification_service.model.NotificationStatus;
import com.sifthub.notification_service.model.NotificationType;

import java.time.LocalDate;
import java.util.List;

public interface StatsRepositoryCustom {

    List<DailyStats> findByDateRangeAndTypeAndStatus(LocalDate startDate, LocalDate endDate,
                                                     NotificationType type, NotificationStatus status);

    void incrementStats(LocalDate date, NotificationType type, NotificationStatus status);
}
