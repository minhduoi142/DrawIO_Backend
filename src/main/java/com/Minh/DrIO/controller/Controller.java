package com.Minh.DrIO.controller;

import java.security.Principal;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Minh.DrIO.Repository.UserRepository;
import com.Minh.DrIO.service.KeywordsService;

@RestController
public class Controller {
@Autowired
    UserRepository userRepository;
@Autowired
KeywordsService service;
@Autowired
    ChatClient chatClient;
    @GetMapping("/user")
    public Principal user(Principal principal)
    {
        System.out.println("username:" +principal.getName());
        return principal;
    }
    // @GetMapping("/abc")
    // public List<User> getuser()
    // {
    //    return  userRepository.findAll();
    // }
    @GetMapping("/check")
    public String testai()
    {
        String prompt1 = "Generate one drawable words for a guessing game, say nothing more.";
        String response1 = chatClient.prompt()
                .messages(new UserMessage(prompt1))
                .call()
                .content();
        return  response1;
    }
    @Autowired
    RedisTemplate<String, String> template;
    @GetMapping("/redis")
    public String redis()
    {
        template.opsForValue().set("test", "hello");
        return template.opsForValue().get("test");
//        return "a";
    }
}
