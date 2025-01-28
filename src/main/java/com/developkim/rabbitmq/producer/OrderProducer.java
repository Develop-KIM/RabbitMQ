package com.developkim.rabbitmq.producer;

import static com.developkim.rabbitmq.config.RabbitMQV7Config.ORDER_TOPIC_EXCHANGE;

import com.developkim.rabbitmq.config.RabbitMQV6Config;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendShipping(String message) {
        rabbitTemplate.convertAndSend(
            ORDER_TOPIC_EXCHANGE,
            "order.completed.*",
            message
        );
        log.info("[주문 완료] 배송 지시 메시지: {}", message);
    }
}
