package com.example.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication: 스프링 부트 애플리케이션의 시작점을 나타내는 어노테이션
@SpringBootApplication
public class WebSocketApplication {
    // 메인 메서드: 애플리케이션의 진입점
    public static void main(String[] args) {
        SpringApplication.run(WebSocketApplication.class, args);
    }
} 