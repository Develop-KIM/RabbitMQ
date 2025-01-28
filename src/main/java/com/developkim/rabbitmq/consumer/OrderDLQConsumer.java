package com.developkim.rabbitmq.consumer;

import static com.developkim.rabbitmq.config.RabbitMQV6Config.DLQ;
import static com.developkim.rabbitmq.config.RabbitMQV6Config.ORDER_EXCHANGE;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderDLQConsumer {

    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = DLQ)
    public void process(String message) {
        log.info("[DLQ 메세지] {}", message);

        try {
            String fixMessage = "success";

            rabbitTemplate.convertAndSend(
                ORDER_EXCHANGE,
                "order.completed.shipping",
                fixMessage
            );
            log.info("[DLQ 전송 ] {}", message);
        } catch (Exception e) {
            log.error("[DLQ 에러 ] {}", e.getMessage());
        }
    }
}
