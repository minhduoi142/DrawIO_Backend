package com.Minh.DrIO.service;
import com.Minh.DrIO.websocket.StrokeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class DrawService {
    @Autowired
    RedisTemplate<String, StrokeMessage> template;
    // cập nhật nét vẽ mới từ client
    public StrokeMessage getStrokeMessage(Integer roomId, StrokeMessage strokeMessage) {
        template.opsForList().rightPush("roomId: " + roomId + " strokes", strokeMessage);
        // cập nhật nét vẽ mới cho các subcriber
        template.convertAndSend("roomId: " + roomId + " update", strokeMessage);
        return strokeMessage;
    }
    // getStroke để gửi, update nét vẽ mới về client của các subcriber
    public List<StrokeMessage> sendStrokeMessage(Integer roomId, StrokeMessage strokeMessage) {
        return template.opsForList().range("roomId: " + roomId + " strokes", 0, -1);
    }
    public String clearStrokes(Integer roomId) {
        template.delete("roomId: " + roomId + " strokes");
        return "roomid:" + roomId + "clear";
    }
}
