package com.Minh.DrIO.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.Minh.DrIO.Repository.RoomPlayerRepository;
import com.Minh.DrIO.model.RoomPlayer;

@Service
public class GameService {
    @Autowired
    private RoomPlayerRepository roomPlayerRepository;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    public boolean checkAnswer(Integer roomId, String answer, String idToken) {
        String currentWord = (String) redisTemplate.opsForValue().get("room:" + roomId + "currentWord");
        if (currentWord != null && currentWord.equals(answer)) {
            RoomPlayer player = null;
            List<RoomPlayer> players = roomPlayerRepository.findByRoomId(roomId);
            for (RoomPlayer rp : players) {
                if (rp.getUser().getIdToken().equals(idToken)) {
                    player = rp;
                    break;
                }
            }
            if (player != null) {
                player.setScore(player.getScore() + 10);
                redisTemplate.opsForSet().add("room: " + roomId + ": correctPlayers", player.getUser().getIdToken());
                roomPlayerRepository.save(player);
                redisTemplate.convertAndSend("room:" + roomId + ":updates",
                        "{\"event\": \"answer_correct\", \"email\": \"" + idToken + "\"}");
            }
            return true;
        }   
        return false;
    }
}