package com.example.websocket.model;

import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class ChatRoom {
    // 채팅방의 고유 식별자
    private String id;
    // 채팅방 이름
    private String name;
    // 채팅방에 참여중인 사용자 목록
    private Set<String> users = new HashSet<>();

    // 채팅방 생성자: 이름을 받아 새로운 채팅방 생성
    public ChatRoom(String name) {
        this.id = UUID.randomUUID().toString(); // 랜덤 UUID 생성
        this.name = name;
    }

    // ID 조회 메서드 (Lombok @Getter로 자동 생성되지만, 명시적으로 추가)
    public String getId() {
        return this.id;
    }

    // 사용자를 채팅방에 추가하는 메서드
    public void addUser(String username) {
        users.add(username);
    }

    // 사용자를 채팅방에서 제거하는 메서드
    public void removeUser(String username) {
        users.remove(username);
    }
} 