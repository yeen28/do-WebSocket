package com.example.websocket.controller;

import com.example.websocket.model.ChatRoom;
import com.example.websocket.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

// 채팅 화면을 제공하는 컨트롤러
@Controller
public class ChatViewController {

    @Autowired
    private ChatService chatService;

    // 메인 화면 - 채팅방 목록 또는 생성 화면
    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    // 채팅방 생성 처리
    @PostMapping("/create-room")
    public String createRoom(@RequestParam("roomName") String roomName) {
        ChatRoom room = chatService.createRoom(roomName);
        return "redirect:/room/" + room.getId();
    }

    // 채팅방 입장 화면
    @GetMapping("/room/{roomId}")
    public String room(@PathVariable String roomId, Model model) {
        model.addAttribute("roomId", roomId);
        // 채팅방이 없으면 새로 생성 (임시 처리)
        if (chatService.getRoom(roomId) == null) {
            chatService.createRoom("Room " + roomId.substring(0, 8));
        }
        return "chat";
    }
} 