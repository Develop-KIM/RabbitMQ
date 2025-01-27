package com.developkim.rabbitmq.publisher;

import static com.developkim.rabbitmq.config.RabbitMQV3Config.FANOUT_EXCHANGE;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(String message) {
        rabbitTemplate.convertAndSend(FANOUT_EXCHANGE, "", message);
        log.info("[RabbitMQ Publish] Notification: {}", message);
    }
}
