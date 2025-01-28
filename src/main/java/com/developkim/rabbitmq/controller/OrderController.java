package com.developkim.rabbitmq.controller;

import com.developkim.rabbitmq.producer.OrderProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/order")
@RestController
public class OrderController {

    private final OrderProducer orderProducer;

    @GetMapping
    public ResponseEntity<String> sendOrderMessage(@RequestParam String message) {
        orderProducer.sendShipping(message);
        return ResponseEntity.ok("[주문 요청]: " + message);
    }
}
