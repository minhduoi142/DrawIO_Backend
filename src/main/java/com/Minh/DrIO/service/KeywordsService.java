package com.Minh.DrIO.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.Minh.DrIO.Repository.RoomRepository;
import com.Minh.DrIO.model.Room;
@Service
public class KeywordsService {
    private final RedisTemplate<String, String> template;
    private final ChatClient chatClient;
    @Autowired
    RoomRepository roomRepository;
    public KeywordsService(ChatClient chatClient, RedisTemplate<String, String> template) {
        this.chatClient = chatClient;
        this.template = template;
    }
    public List<String> generateKeywords(int count, Integer roomid) {
    
        Room room = roomRepository.findById(roomid)
                .orElseThrow(() -> new RuntimeException());
        String prompt1 = "Generate one drawable word for a guessing game about " + room.getTopic()  + "in " + room.getLang()+ ". Say nothing more, no translating, no endline";
        String response1 = chatClient.prompt()
                .messages(new UserMessage(prompt1))
                .call()
                .content();
        template.opsForValue().set("room:" + roomid + "currentWord1", response1);
        String prompt2 = "Generate one drawable word for a guessing game about " + room.getTopic() + " that is completely different from the word " + response1   + "in "+ room.getLang() +". Say nothing more, no translating, ";
        String response2 = chatClient.prompt()
                .messages(new UserMessage(prompt2))
                .call()
                .content();
        template.opsForValue().set("room:" + roomid + "currentWord2", response2);
    List<String> keywords = Arrays.asList(
                (String) template.opsForValue().get("room:" + roomid + "currentWord1"),
                (String) template.opsForValue().get("room:" + roomid + "currentWord2")
        );
        return keywords;
    }
    public String getkeyword(Integer roomid, String wordid) {
        template.delete("room:" + roomid + ":correctPlayers");
        if ("1".equals(wordid)) {
            template.opsForValue().set("room:" + roomid + "currentWord",(String) template.opsForValue().get("room:" + roomid + "currentWord1"));
            return template.opsForValue().get("room:" + roomid + "currentWord");
        } else {
            template.opsForValue().set("room:" + roomid + "currentWord",(String) template.opsForValue().get("room:" + roomid + "currentWord2"));
            return template.opsForValue().get("room:" + roomid + "currentWord");
        }
    }
}
