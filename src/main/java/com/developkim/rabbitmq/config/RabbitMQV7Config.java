package com.developkim.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQV7Config {

    public static final String ORDER_COMPLETED_QUEUE = "orderCompletedQueue";
    public static final String ORDER_TOPIC_EXCHANGE = "order_exchange";
    public static final String ORDER_TOPIC_DLX = "deadLetterExchange";

    public static final String DLQ = "deadLetterQueue";
    public static final String DEAD_LETTER_ROUTING_KEY = "dead.letter";

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_TOPIC_EXCHANGE);
    }

    @Bean
    public TopicExchange deadLetterExchange() {
        return new TopicExchange(ORDER_TOPIC_DLX);
    }

    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable(ORDER_COMPLETED_QUEUE)
            .withArgument("x-dead-letter-exchange", ORDER_TOPIC_DLX)
            .withArgument("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY)
            .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return new Queue(DLQ);
    }

    @Bean
    public Binding orderCompletedBinding() {
        return BindingBuilder.bind(orderQueue()).to(orderExchange()).with("order.completed.*");
    }

    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with(DEAD_LETTER_ROUTING_KEY);
    }
}
