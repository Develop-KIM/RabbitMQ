package com.developkim.rabbitmq.producer;

import static com.developkim.rabbitmq.config.RabbitMQV2Config.QUEUE_NAME;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class WorkQueueProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendWorkQueue(String workQueueMessage, int duration) {
        String message = workQueueMessage + "|" + duration;
        rabbitTemplate.convertAndSend(QUEUE_NAME, message);
        log.info("[RabbitMQ Producer - Send Work Queue] message: {}", message);
    }
}
