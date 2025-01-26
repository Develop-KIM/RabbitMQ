package com.developkim.rabbitmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

// 저장된 걸 소비하는 클래스
@Slf4j
@Component
public class Consumer {

    public void receiveMessage(String message) {
        log.info("[RabbitMQ] Received message: {}", message);
    }
}
