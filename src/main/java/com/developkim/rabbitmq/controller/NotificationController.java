package com.developkim.rabbitmq.controller;

import com.developkim.rabbitmq.publisher.NotificationPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/notification")
@RestController
public class NotificationController {

    private final NotificationPublisher publisher;

    @PostMapping
    public String sendNotification(@RequestBody String message) {
        publisher.publish(message);
        return "[RabbitMQ] Notification sent: " + message + "\n";
    }
}
