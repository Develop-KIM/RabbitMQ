package com.developkim.rabbitmq.producer;

import com.developkim.rabbitmq.config.RabbitMQV9Config;
import com.developkim.rabbitmq.model.StockEntity;
import com.developkim.rabbitmq.repository.StockRepository;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class MessageProducer {

    private final StockRepository stockRepository;
    private final RabbitTemplate rabbitTemplate;

//    @Transactional
//    public void sendMessage(StockEntity stockEntity, String testCase) {
//        rabbitTemplate.execute(channel -> {
//            try {
//                channel.txSelect(); // 트랜잭션 시작
//                stockEntity.setProcessed(false);
//                stockEntity.setCreatedAt(LocalDateTime.now());
//                StockEntity stockSaved = stockRepository.save(stockEntity);
//
//                log.info("[스톡 저장] {}", stockSaved);
//
//                // 메시지 발행
//                rabbitTemplate.convertAndSend("transactionQueue", stockSaved);
//
//                if ("fail".equalsIgnoreCase(testCase)) {
//                    throw new RuntimeException("[트랜잭션 작업 중 에러]");
//                }
//
//                channel.txCommit();
//                log.info("[트랜잭션 정상 처리]");
//            } catch (Exception e) {
//                log.info("[트랜잭션 실패] {}", e.getMessage());
//                channel.txRollback();
//                throw new RuntimeException("[트랜잭션 롤백] {}", e);
//            } finally {
//                if (channel != null) {
//                    try {
//                        channel.close();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            return null;
//        });
//    }

    @Transactional
    public void sendMessage(StockEntity stockEntity, boolean testCase) {
        stockEntity.setProcessed(false);
        stockEntity.setCreatedAt(LocalDateTime.now());
        StockEntity entity = stockRepository.save(stockEntity);

        log.info("[프로듀서 엔티티] {}", entity);

        if (stockEntity.getUserId() == null || stockEntity.getUserId().isEmpty()) {
            throw new RuntimeException("[유저 아이디는 필수입니다.]");
        }

        try {
            // 메시지를 rabbitMQ에 전송
            CorrelationData correlationData = new CorrelationData(entity.getId().toString());
            rabbitTemplate.convertAndSend(
                testCase ? "nonExistentExchange" : RabbitMQV9Config.EXCHANGE_NAME,
                testCase ? "invalidRoutingKey" : RabbitMQV9Config.ROUTING_KEY,
                entity,
                correlationData
            );

            if (correlationData.getFuture().get(5, TimeUnit.SECONDS).isAck()) {
                log.info("[프로듀서 correlationData] 성공 {}", correlationData);
                entity.setProcessed(true);
                stockRepository.save(entity);
            } else {
                throw new RuntimeException("[컨펌 실패 - 롤백]");
            }
        } catch (Exception e) {
            log.info("[프로듀서 예외 실패] {}", e);
            throw new RuntimeException(e);
        }
    }
}
