package com.sifthub.notification_service.controller;

import com.sifthub.notification_service.model.DailyStats;
import com.sifthub.notification_service.model.NotificationRequest;
import com.sifthub.notification_service.model.NotificationStatus;
import com.sifthub.notification_service.model.NotificationType;
import com.sifthub.notification_service.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);
    @Autowired
    private NotificationService notificationService;

    @Operation(summary = "Send multiple notifications",
            description = "Sends a batch of notifications")
    @PostMapping("/send")
    public ResponseEntity<String> sendNotifications(@RequestBody List<NotificationRequest> requests) {
        logger.info("Received request to send {} notifications", requests.size());

        try {
            notificationService.sendNotifications(requests);
            logger.info("Successfully queued {} notifications", requests.size());
            return ResponseEntity.ok("Notifications queued successfully");
        } catch (IllegalArgumentException e) {
            logger.error("Failed to queue notifications: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Get stats for a date range",
            description = "Retrieves stats for the specified date range, inclusive of both start and end dates. Optionally filter by notification type and status.")
    @ApiResponse(responseCode = "200", description = "Successful operation",
            content = @Content(schema = @Schema(implementation = DailyStats.class)))
    @ApiResponse(responseCode = "400", description = "Invalid date range or parameters")
    @GetMapping("/stats")
    public ResponseEntity<List<DailyStats>> getStats(
            @Parameter(description = "Start date in yyyy-MM-dd format", example = "2024-09-14")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date in yyyy-MM-dd format", example = "2024-09-14")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @Parameter(description = "Notification type (optional)")
            @RequestParam(required = false) NotificationType type,
            @Parameter(description = "Notification status (optional)")
            @RequestParam(required = false) NotificationStatus status) {
        logger.info("Received request for stats from {} to {}, type: {}, status: {}", startDate, endDate, type, status);
        try {
            List<DailyStats> stats = notificationService.getStats(startDate, endDate, type, status);
            logger.info("Retrieved {} daily stats", stats.size());
            return ResponseEntity.ok(stats);
        } catch (IllegalArgumentException e) {
            logger.error("Failed to retrieve stats: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }
}
