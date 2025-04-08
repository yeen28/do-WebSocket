package com.example.websocket.config;

import com.example.websocket.model.ChatMessage;
import com.example.websocket.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// WebSocket 이벤트를 처리하는 리스너
@Component
public class WebSocketEventListener {

    // 메시지 전송을 위한 템플릿
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    // 채팅 서비스 주입
    @Autowired
    private ChatService chatService;

    // WebSocket 연결이 종료될 때 실행되는 메서드
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        // 세션에서 사용자 정보 추출
        var headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        String roomId = (String) headerAccessor.getSessionAttributes().get("room");
        
        // 사용자가 채팅방에 있었다면 퇴장 처리
        if (username != null && roomId != null) {
            // 퇴장 메시지 생성
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setType(ChatMessage.MessageType.LEAVE);
            chatMessage.setSender(username);
            chatMessage.setRoom(roomId);
            chatMessage.setContent(username + " left the chat");
            chatMessage.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

            // 채팅방에서 사용자 제거 및 퇴장 메시지 전송
            chatService.removeUserFromRoom(roomId, username);
            messagingTemplate.convertAndSend("/topic/room." + roomId, chatMessage);
        }
    }
} 