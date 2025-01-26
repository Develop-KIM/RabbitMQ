package com.developkim.rabbitmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WorkQueueConsumer {

    public void workQueueTask(String message) {
        String[] messageParts = message.split("\\|");
        String originMessage = messageParts[0];
        int duration = Integer.parseInt(messageParts[1].trim());

        log.info("[RabbitMQ Consumer - Work Queue Task] Received: {} (duration: {}ms)", originMessage, duration);

        try {
            int seconds = duration / 1000;
            for (int i = 0; i < seconds; i++) {
                Thread.sleep(1000);
                log.info(".");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("[RabbitMQ Consumer - Work Queue Task] completed: {}", originMessage);
    }
}
