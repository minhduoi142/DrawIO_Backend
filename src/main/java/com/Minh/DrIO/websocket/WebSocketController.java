package com.Minh.DrIO.websocket;
import com.Minh.DrIO.service.DrawService;
import com.Minh.DrIO.service.GameService;
import com.Minh.DrIO.service.KeywordsService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
@Controller
public class WebSocketController {
    @Autowired
    private DrawService drawService;
    @Autowired
    KeywordsService service;
    @Autowired
    private GameService gameService;
    /**
     *
     * @param roomId id phòng
     * @param strokeMessage dữ liệu nét vẽ
     * @return về data nét vẽ
     */
    @MessageMapping("/room/{roomId}/submitAnswer")
    @SendTo("/topic/room/{roomId}")
    public AnswerMessage submitAnswer(@DestinationVariable Integer roomId, @Payload AnswerMessage answerMessage) {
        boolean isCorrect = gameService.checkAnswer(roomId, answerMessage.getAnswer(), answerMessage.getIdToken());
        answerMessage.setCorrect(isCorrect);
        return answerMessage;
    }
    @MessageMapping("/room/{roomid}/genkeywords")
    @SendTo("/topic/room/{roomid}")
    public String genkeyword(@DestinationVariable Integer roomid) {
        service.generateKeywords(1, roomid);
        return "succeed";
    }
    @MessageMapping("/room/{roomid}/getkeyword/{wordid}")
    @SendTo("/topic/room/{roomid}")
    public String getkeyword(@DestinationVariable Integer roomid, @DestinationVariable String wordid)
    {
        return service.getkeyword(roomid, wordid);
    }
}
