package com.Minh.DrIO.model;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatusCode;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private int id;

//khai báo roomPlayerId để lưu data người chơi trong 1 room
@OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
   @JsonManagedReference
    List<RoomPlayer> roomPlayers;
private String topic;
private int totalPlayers;
    private int maxPlayer;
    private boolean isPrivate;
    @Column(nullable = true)
    private int targetPoint;
    @Column(name = "code_room", nullable = true)
    private String codeRoom;
    private String lang;

}
