package com.example.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

// WebSocket 설정을 위한 설정 클래스
@Configuration
// WebSocket 메시지 브로커 활성화
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // 메시지 브로커 설정 메서드
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // /topic으로 시작하는 주제를 구독할 수 있도록 설정
        config.enableSimpleBroker("/topic");
        // 클라이언트에서 서버로 메시지를 보낼 때 사용할 접두사 설정
        config.setApplicationDestinationPrefixes("/app");
    }

    // STOMP 엔드포인트 등록 메서드
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // /ws 엔드포인트를 등록하고 모든 도메인에서의 접근을 허용
        // SockJS를 활성화하여 WebSocket을 지원하지 않는 브라우저에서도 동작하도록 설정
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")  // 모든 도메인에서 접근 허용 (운영 환경에서는 특정 도메인만 허용하는 것이 좋음)
                .withSockJS();
    }
} 