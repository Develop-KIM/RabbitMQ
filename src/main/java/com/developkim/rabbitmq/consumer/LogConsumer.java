package com.developkim.rabbitmq.consumer;

import static com.developkim.rabbitmq.config.RabbitMQV5Config.ALL_LOG_QUEUE;
import static com.developkim.rabbitmq.config.RabbitMQV5Config.ERROR_QUEUE;
import static com.developkim.rabbitmq.config.RabbitMQV5Config.INFO_QUEUE;
import static com.developkim.rabbitmq.config.RabbitMQV5Config.WARN_QUEUE;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogConsumer {

    @RabbitListener(queues = ERROR_QUEUE)
    public void consumeError(String message) {
        log.error("[ERROR] : {}", message);
    }

    @RabbitListener(queues = WARN_QUEUE)
    public void consumeWarn(String message) {
        log.warn("[WARN ] : {}", message);
    }

    @RabbitListener(queues = INFO_QUEUE)
    public void consumeInfo(String message) {
        log.info("[INFO ] : {}", message);
    }

    @RabbitListener(queues = ALL_LOG_QUEUE)
    public void consumeAllLog(String message) {
        log.info("[ALL LOG] : {}", message);
    }
}
