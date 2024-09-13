package com.sifthub.notification_service.repository.impl;

import com.sifthub.notification_service.model.DailyStats;
import com.sifthub.notification_service.model.NotificationStatus;
import com.sifthub.notification_service.model.NotificationType;
import com.sifthub.notification_service.repository.StatsRepositoryCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class StatsRepositoryImpl implements StatsRepositoryCustom {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<DailyStats> findByDateRangeAndTypeAndStatus(LocalDate startDate, LocalDate endDate,
                                                            NotificationType type, NotificationStatus status) {
        Query query = new Query();
        query.addCriteria(Criteria.where("date").gte(startDate).lte(endDate));

        if (type != null) {
            query.addCriteria(Criteria.where("type").is(type));
        }

        if (status != null) {
            query.addCriteria(Criteria.where("status").is(status));
        }

        return mongoTemplate.find(query, DailyStats.class);
    }

    @Override
    public void incrementStats(LocalDate date, NotificationType type, NotificationStatus status) {
        Query query = new Query(Criteria.where("date").is(date)
                .and("type").is(type)
                .and("status").is(status));

        Update update = new Update().inc("count", 1);

        mongoTemplate.upsert(query, update, DailyStats.class);
    }
}