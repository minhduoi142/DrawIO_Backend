package com.Minh.DrIO.websocket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
class AnswerMessage {
    private String  answer;
    private  String idToken;
    private String  email;
    private boolean isCorrect;
}