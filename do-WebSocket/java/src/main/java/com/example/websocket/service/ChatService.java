package com.example.websocket.service;

import com.example.websocket.model.ChatRoom;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// 채팅 관련 비즈니스 로직을 처리하는 서비스
@Service
public class ChatService {
    // 채팅방 목록을 저장하는 동시성 지원 맵
    private final Map<String, ChatRoom> chatRooms = new ConcurrentHashMap<>();

    // 새로운 채팅방을 생성하는 메서드
    public ChatRoom createRoom(String name) {
        ChatRoom chatRoom = new ChatRoom(name);
        chatRooms.put(chatRoom.getId(), chatRoom);
        return chatRoom;
    }

    // 채팅방 정보를 조회하는 메서드
    public ChatRoom getRoom(String roomId) {
        return chatRooms.get(roomId);
    }

    // 사용자를 채팅방에 추가하는 메서드
    public void addUserToRoom(String roomId, String username) {
        ChatRoom room = chatRooms.get(roomId);
        if (room != null) {
            room.addUser(username);
        }
    }

    // 사용자를 채팅방에서 제거하는 메서드
    // 만약 채팅방에 아무도 없다면 채팅방도 삭제
    public void removeUserFromRoom(String roomId, String username) {
        ChatRoom room = chatRooms.get(roomId);
        if (room != null) {
            room.removeUser(username);
            if (room.getUsers().isEmpty()) {
                chatRooms.remove(roomId);
            }
        }
    }
} 