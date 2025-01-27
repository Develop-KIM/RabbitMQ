package com.developkim.rabbitmq.publisher;

import static com.developkim.rabbitmq.config.RabbitMQV4Config.FANOUT_EXCHANGE_FOR_NEWS;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class NewsPublisher {

    private final RabbitTemplate rabbitTemplate;

    public String publish(String news) {
        return publicMessage(news, " 관련 새 소식이 있어요.");
    }

    public String publishAPI(String news) {
        return publicMessage(news, " - 관련 새 소식이 나왔습니다. (API)");
    }

    private String publicMessage(String news, String messageSuffix) {
        String message = news + messageSuffix;
        rabbitTemplate.convertAndSend(FANOUT_EXCHANGE_FOR_NEWS, news, message);
        log.info("[News publisher] Message published: {}", message);
        return message;
    }
}
