package com.developkim.rabbitmq.controller;

import com.developkim.rabbitmq.producer.WorkQueueProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class WorkQueueController {

    private final WorkQueueProducer workQueueProducer;

    @PostMapping("/workqueue")
    public String workQueue(@RequestParam String message, @RequestParam int duration) {
        workQueueProducer.sendWorkQueue(message, duration);
        return "[RabbitMQ Work Queue] Message sent = " + message + ", (" + duration + ")";
    }
}

/*
curl -X POST "http://localhost:8080/api/workqueue?message=Task1&duration=2000"
curl -X POST "http://localhost:8080/api/workqueue?message=Task2&duration=4000"
curl -X POST "http://localhost:8080/api/workqueue?message=Task3&duration=5000"
*/
