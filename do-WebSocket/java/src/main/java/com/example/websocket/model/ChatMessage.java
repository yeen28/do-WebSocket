package com.example.websocket.model;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// Lombok 어노테이션을 사용하여 getter, setter, 생성자 자동 생성
@Getter
@Setter
@NoArgsConstructor  // 기본 생성자
@AllArgsConstructor // 모든 필드를 파라미터로 받는 생성자
public class ChatMessage {
    // 메시지 타입 (채팅, 입장, 퇴장, 파일)
    private MessageType type;
    // 메시지 내용
    private String content;
    // 발신자 이름
    private String sender;
    // 채팅방 ID
    private String room;
    // 메시지 전송 시간
    private String timestamp;

    // 메시지 타입을 정의하는 열거형
    public enum MessageType {
        CHAT,   // 일반 채팅
        JOIN,   // 입장
        LEAVE,  // 퇴장
        FILE    // 파일 전송
    }
} 