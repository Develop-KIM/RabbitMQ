package com.developkim.rabbitmq.controller;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.developkim.rabbitmq.model.StockEntity;
import com.developkim.rabbitmq.producer.MessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/message")
@RestController
public class TransactionController {

    private final MessageProducer messageProducer;

//    @PostMapping
//    public ResponseEntity<String> sendMessage(
//        @RequestBody StockEntity stockEntity,
//        @RequestParam(required = false, defaultValue = "success") String testcase
//    ) {
//      log.info("[메시지 보내기] {}", stockEntity);
//
//      try {
//          messageProducer.sendMessage(stockEntity, testcase);
//          return ResponseEntity.ok("[메세지 보내기 성공]");
//      } catch (RuntimeException e) {
//          return ResponseEntity.status(INTERNAL_SERVER_ERROR)
//              .body("[MQ 트랜잭션 실패] " + e.getMessage());
//      }
//    }

    @PostMapping
    public ResponseEntity<String> sendMessage(
        @RequestBody StockEntity stockEntity,
        @RequestParam boolean testCase
    ) {
        log.info("[메시지 보내기] {}", stockEntity);

        try {
            messageProducer.sendMessage(stockEntity, testCase);
            return ResponseEntity.ok("[메세지 보내기 성공]");
        } catch (RuntimeException e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body("[MQ 트랜잭션 실패] " + e.getMessage());
        }
    }
}
