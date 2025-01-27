package com.developkim.rabbitmq.controller;

import com.developkim.rabbitmq.publisher.NewsPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@RequiredArgsConstructor
@Controller
public class NewsController {

    private final NewsPublisher publisher;

    @MessageMapping("/subscribe")
    public void handleSubscribe(@Header("newsType") String newsType) {
        log.info("[NewsType   ]: {}", newsType);
        String newsMessage = publisher.publish(newsType);
        log.info("[newsMessage]: {}", newsMessage);
    }
}
