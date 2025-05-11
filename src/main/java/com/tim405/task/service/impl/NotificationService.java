package com.tim405.task.service.impl;


import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService {
    private final JavaMailSender mailSender;

    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendStatusChangeNotification(Long taskId, String newStatus) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("tim4054@gmail.com");
        message.setTo("tim405y@yandex.ru");
        message.setSubject("Task Status Update");
        message.setText(String.format(
                "Task ID: %d\nNew Status: %s\nTime: %s",
                taskId, newStatus, LocalDateTime.now()
        ));
        mailSender.send(message);
    }
}