package com.developkim.rabbitmq.retry;

import static com.developkim.rabbitmq.config.RabbitMQV7Config.DLQ;
import static com.developkim.rabbitmq.config.RabbitMQV7Config.ORDER_TOPIC_EXCHANGE;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderDeadLetterRetryV2 {

    private final RabbitTemplate rabbitTemplate;
    private final RetryTemplate retryTemplate;

@RabbitListener(queues = DLQ)
    public void consume(String failedMessage) {
        retryTemplate.execute(context -> {
            try {
                log.info("[DLQ 리시브] {}", failedMessage);

                String message = "success";
                rabbitTemplate.convertAndSend(ORDER_TOPIC_EXCHANGE, "order.completed.shipping", message);
            } catch (Exception e) {
                log.error("[메시지 에러] {}", e);
            }
            return null;
        });
    }
}
