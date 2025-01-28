package com.developkim.rabbitmq.consumer;

import static com.developkim.rabbitmq.config.RabbitMQV7Config.ORDER_COMPLETED_QUEUE;

import com.rabbitmq.client.Channel;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderConsumer {

    private int retryCount = 0;

    @RabbitListener(queues = ORDER_COMPLETED_QUEUE)
    public void processOrder(String message) {
        log.info("[리시브 메시지] {}, [횟수] {}", message, retryCount);
        if ("fail".equalsIgnoreCase(message)) {
            throw new RuntimeException("[프로세싱 실패 재시도]");
        }
        log.info("[메시지 성공] {}", message);
    }
}
