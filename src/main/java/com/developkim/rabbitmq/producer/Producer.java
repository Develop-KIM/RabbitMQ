package com.developkim.rabbitmq.producer;

import static com.developkim.rabbitmq.config.RabbitMQV1Config.QUEUE_NAME;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

// 큐에 저장하는 클래스
@Slf4j
@RequiredArgsConstructor
@Component
public class Producer {

    private final RabbitTemplate rabbitTemplate;

    public void send(String message) {
        rabbitTemplate.convertAndSend(QUEUE_NAME, message);
        log.info("[RabbitMQ] Message sent to queue: {}", message);
    }
}
