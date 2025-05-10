package com.tim405.task.kafka;

import com.tim405.task.service.impl.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service

public class TaskStatusConsumer {
    private final NotificationService notificationService;

    public TaskStatusConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "task-updates", groupId = "task-group")
    public void listenStatusUpdates(
            @Payload String status,
            @Header(KafkaHeaders.RECEIVED_KEY) String taskId
    ) {
        notificationService.sendStatusChangeNotification(
                Long.parseLong(taskId),
                status
        );
    }
}