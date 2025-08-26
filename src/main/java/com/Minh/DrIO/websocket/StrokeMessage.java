package com.Minh.DrIO.websocket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StrokeMessage {
    private String roomId;
    private String roomplayerid;
    private float startX;
    private float startY;
    private float endX;
    private float endY;
    private String color;
    private float lineWidth;
}