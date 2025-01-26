package com.developkim.rabbitmq.controller;

import com.developkim.rabbitmq.producer.Producer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/message")
@RestController
public class MessageController {

    private final Producer sender;

    @PostMapping("/send")
    public String sendMessage(@RequestBody String message) {
        sender.send(message);
        return "[RabbitMQ] Message sent 성공 " + message;
    }
}
