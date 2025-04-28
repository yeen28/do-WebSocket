package com.example.websocket.controller;

import com.example.websocket.model.ChatMessage;
import com.example.websocket.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// WebSocket 메시지를 처리하는 컨트롤러
@Controller
public class ChatController {

    // 메시지 전송을 위한 템플릿
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // 채팅 서비스 주입
    @Autowired
    private ChatService chatService;

    // 채팅 메시지 전송을 처리하는 메서드
    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        // 현재 시간을 타임스탬프로 설정
        chatMessage.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        // 특정 채팅방으로 메시지 전송
        messagingTemplate.convertAndSend("/topic/room." + chatMessage.getRoom(), chatMessage);
    }

    // 사용자가 채팅방에 입장할 때 처리하는 메서드
    @MessageMapping("/chat.addUser")
    public void addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        String username = chatMessage.getSender();
        String roomId = chatMessage.getRoom();
        
        // 세션에 사용자 정보 저장
        headerAccessor.getSessionAttributes().put("username", username);
        headerAccessor.getSessionAttributes().put("room", roomId);
        
        // 입장 메시지 설정
        chatMessage.setType(ChatMessage.MessageType.JOIN);
        chatMessage.setContent(username + " joined the chat");
        chatMessage.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        // 채팅방에 사용자 추가 및 입장 메시지 전송
        chatService.addUserToRoom(roomId, username);
        messagingTemplate.convertAndSend("/topic/room." + roomId, chatMessage);
    }
} 