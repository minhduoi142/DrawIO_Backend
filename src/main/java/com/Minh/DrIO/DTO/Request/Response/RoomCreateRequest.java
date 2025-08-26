package com.Minh.DrIO.DTO.Request.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomCreateRequest {
    private int maxPlayer;
    private String topic;
    private boolean isPrivate;
    private int targetPoint;
    private String lang;

    }
