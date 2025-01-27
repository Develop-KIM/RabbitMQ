package com.developkim.rabbitmq.exception;

import com.developkim.rabbitmq.publisher.LogPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomExceptionHandler {

    private final LogPublisher publisher;

    // 에러나 로그 처리
    public void handleException(Exception e) {
        String message = e.getMessage();

        String routingKey;

        if (e instanceof NullPointerException) {
            routingKey = "log.error";
        } else if (e instanceof IllegalArgumentException) {
            routingKey = "log.warn";
        } else {
            routingKey = "log.error";
        }

        publisher.publish(routingKey, "[Exception] : " + message);
    }

    // 메세지 처리
    public void handleMessage(String message) {
        String routingKey = "log.info";
        publisher.publish(routingKey, "[INFO LOG] : " + message);
    }
}
