package com.Minh.DrIO.DTO.Request.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomResponse {
    private int id;
    private String topic;
    private int totalplayer;
    private int maxPlayer;
    private int targetpoint;
    private String lang;
}
