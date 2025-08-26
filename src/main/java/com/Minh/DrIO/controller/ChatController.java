package com.Minh.DrIO.controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Minh.DrIO.service.KeywordsService;

import io.swagger.v3.oas.annotations.Operation;
@RestController
@RequestMapping("/chat")
public class ChatController {
    RedisTemplate<String, String> template;
    private final ChatClient chatClient;
    public ChatController(ChatClient chatClient, KeywordsService keywordsService) {
        this.chatClient = chatClient;
       this.keywordsService = keywordsService;
    }
     @Autowired
    KeywordsService keywordsService;
    @Operation(summary = "Gen ra 2 từ khóa để chọn")
     @PostMapping("/genkeywords/{roomid}")
    public ResponseEntity<?> genKeywords(@RequestParam(defaultValue = "1") int count, @PathVariable Integer roomid)
    {
        try{
        return ResponseEntity.ok(keywordsService.generateKeywords(count, roomid));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body("Failed: " + e.getMessage());
        }
    }
    @Operation(summary = "Chọn 1 từ khóa")
    @GetMapping("/getkeywords/{roomid}/{wordid}")
    public ResponseEntity<?> getkeywords(@PathVariable Integer roomid, @PathVariable String wordid) {
        try {
         return  ResponseEntity.ok(keywordsService.getkeyword(roomid, wordid));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(404).body("Failed: " + e.getMessage());
        }
    }
}