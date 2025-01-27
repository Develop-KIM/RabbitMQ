package com.developkim.rabbitmq.subscribe;

import static com.developkim.rabbitmq.config.RabbitMQV4Config.JAVA_QUEUE;
import static com.developkim.rabbitmq.config.RabbitMQV4Config.SPRING_QUEUE;
import static com.developkim.rabbitmq.config.RabbitMQV4Config.VUE_QUEUE;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class NewsSubscriber {

    private final SimpMessagingTemplate messagingTemplate;

    @RabbitListener(queues = JAVA_QUEUE)
    public void javaNews(String message) {
        messagingTemplate.convertAndSend("/topic/java", message);
    }

    @RabbitListener(queues = SPRING_QUEUE)
    public void springNews(String message) {
        messagingTemplate.convertAndSend("/topic/spring", message);
    }

    @RabbitListener(queues = VUE_QUEUE)
    public void vueNews(String message) {
        messagingTemplate.convertAndSend("/topic/vue", message);
    }
}
