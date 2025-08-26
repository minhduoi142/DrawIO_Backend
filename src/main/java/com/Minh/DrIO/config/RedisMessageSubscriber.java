package com.Minh.DrIO.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
@Component
public class RedisMessageSubscriber implements MessageListener {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String channel = new String(pattern);
            String roomId = channel.split(":")[1]; // room:{roomId}:updates
                String messageBody = new String(message.getBody());
            messagingTemplate.convertAndSend("/topic/room/" + roomId, messageBody);
        } catch (Exception e) {
            System.err.println("Error processing Redis message: " + e.getMessage());
        }
    }
}