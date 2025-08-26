package com.Minh.DrIO.chat;

import com.Minh.DrIO.Repository.UserRepository;
import com.Minh.DrIO.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.util.HtmlUtils;

import java.util.Random;

@Controller
public class ChatMessageController {
    @Autowired
    UserRepository repository;
    @Autowired
    RedisTemplate <String, String>template;
    Random random = new Random();
    @MessageMapping("/room/{roomid}/chat-sendMessage")
    @SendTo("/topic/room/{roomid}")
    public ResponseEntity<Greeting> greeting(@DestinationVariable Integer roomId, HelloMessage message) throws Exception {
        if (template.opsForSet().isMember("room: " + roomId + ": correctPlayers", message.getIdToken())) {
            User user = (User) template.opsForHash().get("users", message.getIdToken());
            if (user != null) {
                Greeting greeting = new Greeting(user.getNickname() + ": " + HtmlUtils.htmlEscape(message.getContent()));
                return ResponseEntity.ok(greeting);
            }
        }
        return ResponseEntity.notFound().build();
    }
}