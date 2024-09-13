package com.sifthub.notification_service.repository;

import com.sifthub.notification_service.model.DailyStats;
import com.sifthub.notification_service.model.NotificationStatus;
import com.sifthub.notification_service.model.NotificationType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StatsRepository extends MongoRepository<DailyStats, String>, StatsRepositoryCustom {
}

