package com.sifthub.notification_service.service;

import com.sifthub.notification_service.model.*;
import com.sifthub.notification_service.repository.NotificationRepository;
import com.sifthub.notification_service.repository.StatsRepository;
import com.sifthub.notification_service.strategy.NotificationFactory;
import com.sifthub.notification_service.strategy.NotificationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@EnableAsync
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private StatsRepository statsRepository;

    private final ConcurrentLinkedQueue<Notification> notificationQueue = new ConcurrentLinkedQueue<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    /*
     * This method is used to send notifications to the notification queue.
     * It is an asynchronous method that returns immediately after adding the notifications to the queue.
     */
    @Async
    public void sendNotifications(List<NotificationRequest> requests) {
        logger.info("Received {} notification requests", requests.size());
        for (NotificationRequest request : requests) {
            Notification notification = new Notification(
                    request.getUserId(),
                    request.getType(),
                    request.getContent(),
                    NotificationStatus.PENDING,
                    LocalDateTime.now()
            );
            notificationQueue.offer(notification);
            logger.debug("Added notification to queue: {}", notification);
        }
        processNotifications();
    }

    /*
     * This method processes notifications from the queue using an ExecutorService.
     * The ExecutorService manages a thread pool, allowing for concurrent processing of notifications.
     */
    private void processNotifications() {
        logger.info("Starting to process notifications");
        while (!notificationQueue.isEmpty()) {
            Notification notification = notificationQueue.poll();
            if (notification != null) {
                executorService.submit(() -> processNotification(notification));
            }
        }
        logger.info("Finished processing notifications");
    }

    /*
     * This method processes a single notification and updates the statistics for the notification.
     */
    private void processNotification(Notification notification) {
        logger.debug("Processing notification: {}", notification);

        NotificationStrategy strategy = NotificationFactory.getStrategy(notification.getType());
        boolean success = strategy.send(notification.getUserId(), notification.getContent());

        notification.setStatus(success ? NotificationStatus.SUCCESS : NotificationStatus.FAILURE);
        notificationRepository.save(notification);
        logger.info("Notification processed. Status: {}", notification.getStatus());
        updateStats(notification);
    }

    /*
     * This method retrieves statistics for a specified date range and optional notification type and status.
     */
    public List<DailyStats> getStats(LocalDate startDate, LocalDate endDate, NotificationType type, NotificationStatus status) {
        logger.info("Fetching stats from {} to {}, type: {}, status: {}", startDate, endDate, type, status);
        if (startDate == null || endDate == null) {
            logger.error("Invalid date range: start date or end date is null");
            throw new IllegalArgumentException("Both start date and end date must be provided");
        }
        // Ensure startDate is not after endDate
        if (startDate.isAfter(endDate)) {
            logger.error("Invalid date range: start date {} is after end date {}", startDate, endDate);
            throw new IllegalArgumentException("start date is after end date");
        }

        List<DailyStats> stats = statsRepository.findByDateRangeAndTypeAndStatus(startDate, endDate, type, status);
        logger.debug("Retrieved {} daily stats", stats.size());
        return stats;
    }

    /*
     * This method updates the statistics for a notification.
     */
    private void updateStats(Notification notification) {
        logger.debug("Updating stats for notification: {}", notification);

        statsRepository.incrementStats(
                notification.getTimestamp().toLocalDate(),
                notification.getType(),
                notification.getStatus()
        );
    }
}
