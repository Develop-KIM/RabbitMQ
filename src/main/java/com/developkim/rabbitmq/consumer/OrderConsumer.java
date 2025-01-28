package com.developkim.rabbitmq.consumer;

import static com.developkim.rabbitmq.config.RabbitMQV6Config.ORDER_COMPLETED_QUEUE;

import com.rabbitmq.client.Channel;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderConsumer {

    private final int MAX_RETRIES = 3;
    private int retryCount = 0;

    @RabbitListener(queues = ORDER_COMPLETED_QUEUE, containerFactory = "rabbitListenerContainerFactory")
    public void processOrder(String message, Channel channel, @Header("amqp_deliveryTag") long tag) {
        try {
            // 실패 유발
            if ("fail".equalsIgnoreCase(message)) {
                if (retryCount < MAX_RETRIES) {
                    log.error("[재시도  ] 재시도 횟수: {}", retryCount);
                    retryCount++;
                    throw new RuntimeException(message);
                } else {
                    log.error("[재시도  ] 최대 횟수 초과, DLQ 이동 시킴");
                    retryCount = 0;
                    channel.basicNack(tag, false, false);
                    return;
                }
            }
            log.info("[주문 성공] {}", message);
            channel.basicAck(tag, false);
            retryCount = 0;
        } catch (Exception e) {
            log.error("[주문 실패] {}", e.getMessage());
            try {
                // 실패 시 basicReject 재처리 전송
                channel.basicReject(tag, true);
            } catch (IOException ex) {
                log.error("[처리 실패] {}", ex.getMessage());
            }
        }
    }
}
