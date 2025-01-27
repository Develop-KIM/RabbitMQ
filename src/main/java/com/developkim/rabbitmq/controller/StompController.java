package com.developkim.rabbitmq.controller;

import com.developkim.rabbitmq.model.NotificationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class StompController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/send")
    public void sendMessage(NotificationMessage notificationMessage) {
        log.info("Sending message: {}", notificationMessage);
        // 수신된 메시지를 브로드캐스팅
        String message = notificationMessage.getMessage();
        log.info("[STOMP] sending message: {}", message);

        // 클라이언트에 메시지 브로드캐스트
        messagingTemplate.convertAndSend("/topic/notification", message);
    }
}
