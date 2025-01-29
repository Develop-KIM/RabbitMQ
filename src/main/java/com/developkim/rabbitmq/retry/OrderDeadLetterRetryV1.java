package com.developkim.rabbitmq.retry;

import static com.developkim.rabbitmq.config.RabbitMQV6Config.DLQ;
import static com.developkim.rabbitmq.config.RabbitMQV7Config.ORDER_TOPIC_EXCHANGE;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderDeadLetterRetryV1 {

    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = DLQ)
    public void process(String message) {
        log.info("[DLQ 메세지] {}", message);

        try {
            // "fail" 메시지를 수정하여 성공적으로 처리되도록 변경
            if ("fail".equalsIgnoreCase(message)) {
                message = "success";
                log.info("[DLQ 메세지 처리 O] {}", message);
            } else {
                // 이미 수정된 메세지는 다시 처리하지 않음
                log.error("[DLQ 메세지 처리 X] {}");
                return;
            }

            // 수정된 메시지를 원래 큐로 다시 전송
            rabbitTemplate.convertAndSend(ORDER_TOPIC_EXCHANGE, "order.completed", message);
            log.info("[DLQ 전송 ] {}", message);
        } catch (Exception e) {
            log.error("[DLQ 에러 ] {}", e.getMessage());
        }
    }
}
