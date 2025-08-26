package com.Minh.DrIO.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
@Configuration
@EnableWebSocketMessageBroker
@EnableWebSocket
// cấu hình websocket với giao thức chuẩn STOMP trong xử lí realtime
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
@Override
// method tạo nơi gửi và nhận message
    public void configureMessageBroker(MessageBrokerRegistry config)
    {    // gửi message đến subcriber có đuối "/topic"
        config.enableSimpleBroker("/topic");
        // thiết lập publisher có đuôi "/app"
        config.setApplicationDestinationPrefixes("/app");
    }
    @Override
    // đăng lí endpoint(optional)
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/gs-guide-websocket").setAllowedOrigins("*");
    }
}
