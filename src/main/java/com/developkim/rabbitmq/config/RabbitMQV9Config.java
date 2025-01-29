package com.developkim.rabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RabbitMQV9Config {


    public static final String QUEUE_NAME = "transactionQueue";
    public static final String EXCHANGE_NAME = "transactionExchange";
    public static final String ROUTING_KEY = "transactionRoutingKey";

    @Bean
    public Queue transactionQueue() {
        return QueueBuilder.durable(QUEUE_NAME)
            .withArgument("x-dead-letter-exchange", "")
            .withArgument("x-dead-letter-routing-key", "deadLetterQueue")
            .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return new Queue("deadLetterQueue");
    }

    @Bean
    public DirectExchange transactionExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding transactionBinding(Queue transactionQueue, DirectExchange transactionExchange) {
        return BindingBuilder.bind(transactionQueue).to(transactionExchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        rabbitTemplate.setMandatory(true);

        // ConfirmCallback 설정
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.info("[메시지 컨펌O] {}", correlationData != null ? correlationData.getId() : "null");
            } else {
                log.info("[메시지 컨펌X] {}, reason: {}", correlationData != null ? correlationData.getId() : "null", cause);

                // 실제 메시지가 처리 실패했을 경우 처리 로직 작성 (예시: 로그 기록, db 저장, 알림)
            }
        });

        // ReturnCallback 설정
        rabbitTemplate.setReturnsCallback(returned -> {
            log.info("[메시지 반환] {}", returned.getMessage().getBody());
            log.info("[ 익스체인지] {}", returned.getExchange());
            log.info("[  라우팅키] {}", returned.getRoutingKey());
        });
        return rabbitTemplate;
    }

    // RabbitListener 설정, 수동 Ack 모드 설정
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactoryV3(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }
}
