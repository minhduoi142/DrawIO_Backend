package com.Minh.DrIO.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;

import com.Minh.DrIO.service.DrawService;
import com.Minh.DrIO.websocket.StrokeMessage;
public class DrawController {
    @Autowired
    DrawService drawService;
    @MessageMapping("/room/{roomId}/draw/send")
    @SendTo("/topic/room/{roomId}")
    public StrokeMessage getStrokeMessage(@DestinationVariable Integer roomId, @Payload StrokeMessage strokeMessage) {
        drawService.getStrokeMessage(roomId, strokeMessage);
        return strokeMessage;
    }
    // Gửi nhiều stroke (trường hợp undo/redo hoặc sync toàn bộ)
    @MessageMapping("/room/{roomId}/draw/batch")
    @SendTo("/topic/room/{roomId}")
    public List<StrokeMessage> sendStroke(@DestinationVariable Integer roomId, @Payload StrokeMessage strokeMessage) {
        return drawService.sendStrokeMessage(roomId, strokeMessage);
    }

    // Xóa hết stroke
    @MessageMapping("/room/{roomId}/draw/clear")
    @SendTo("/topic/room/{roomId}")
    public String clearStroke(@DestinationVariable Integer roomId) {
        return drawService.clearStrokes(roomId);
    }
}
