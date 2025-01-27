package com.developkim.rabbitmq.publisher;

    import static com.developkim.rabbitmq.config.RabbitMQV5Config.TOPIC_EXCHANGE;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class LogPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(String routingKey, String message) {
        rabbitTemplate.convertAndSend(TOPIC_EXCHANGE, routingKey, message);
        log.info("[LOG - PUBLISHER] exchange: {}, routingKey: {}, message: {}", TOPIC_EXCHANGE, routingKey, message);
    }
}
