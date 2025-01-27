package com.developkim.rabbitmq.subscribe;

import static com.developkim.rabbitmq.config.RabbitMQV3Config.QUEUE_NANE;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationSubscriber {

    public static final String CLIENT_URL = "/topic/notification";

    private final SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = QUEUE_NANE)
    public void subscriber(String message) {
        log.info("[RabbitMQ Subscriber] Notification: {}", message);
        messagingTemplate.convertAndSend(CLIENT_URL, message);
    }
}
