package com.developkim.rabbitmq.consumer;

import static com.developkim.rabbitmq.config.RabbitMQV9Config.QUEUE_NAME;

import com.developkim.rabbitmq.model.StockEntity;
import com.developkim.rabbitmq.repository.StockRepository;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class MessageConsumer {

    private final StockRepository stockRepository;

//    @RabbitListener(queues = "transactionQueue", containerFactory = "rabbitListenerContainerFactory1")
//    public void receiveTransaction(StockEntity stockEntity) {
//        log.info("[리시브 메시지] {}", stockEntity);
//
//        try {
//            stockEntity.setProcessed(true);
//            stockEntity.setUpdatedAt(LocalDateTime.now());
//            stockRepository.save(stockEntity);
//            log.info("[저장 완료]");
//        } catch (Exception e) {
//            log.info("[수정 에러] {}", e.getMessage());
//            throw e; // 메시지를 데이레터 큐에 집어넣는다.
//        }
//    }

    @RabbitListener(queues = QUEUE_NAME, containerFactory = "rabbitListenerContainerFactoryV3")
    public void receiveTransaction(StockEntity stock, @Header(AmqpHeaders.DELIVERY_TAG) long tag, Channel channel) {
        try {
            log.info("[컨슈머 시작] {}", stock);
            Optional<StockEntity> optionalStock = stockRepository.findById(stock.getId());
            if (optionalStock.isPresent()) {
                StockEntity stockEntity = optionalStock.get();
                stockEntity.setUpdatedAt(LocalDateTime.now());
                stockRepository.save(stockEntity);
                log.info("[저장 완료]");
            } else {
                throw new RuntimeException("[스톡 404]");
            }
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.info("[컨슈머 에러] {}", e.getMessage());
            try {
                channel.basicNack(tag, false, false);
            } catch (IOException ex) {
                log.info("[컨슈머 에크 실패] {}", ex.getMessage());
                throw new RuntimeException(ex);
            }
        }
    }
}
