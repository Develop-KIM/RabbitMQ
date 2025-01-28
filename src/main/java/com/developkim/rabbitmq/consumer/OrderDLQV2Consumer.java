package com.developkim.rabbitmq.consumer;

import static com.developkim.rabbitmq.config.RabbitMQV7Config.DEAD_LETTER_ROUTING_KEY;
import static com.developkim.rabbitmq.config.RabbitMQV7Config.ORDER_COMPLETED_QUEUE;
import static com.developkim.rabbitmq.config.RabbitMQV7Config.ORDER_TOPIC_DLX;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderDLQV2Consumer {

    private final RabbitTemplate rabbitTemplate;
    private final RetryTemplate retryTemplate;

    @RabbitListener(queues = ORDER_COMPLETED_QUEUE)
    public void consume(String message) {
        retryTemplate.execute(context -> {
            try {
                log.info("[리시브 메시지] {}, [재시도] {}", message, context.getRetryCount());

                if ("fail".equalsIgnoreCase(message)) {
                    throw new RuntimeException(message);
                }
                log.info("[메시지 처리 성공] {}", message);
            } catch (Exception e) {
                if (context.getRetryCount() >= 2) {
                    rabbitTemplate.convertAndSend(ORDER_TOPIC_DLX, DEAD_LETTER_ROUTING_KEY, message);
                } else {
                    throw e;
                }
            }
            return null;
        });
    }
}
