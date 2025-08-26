package com.Minh.DrIO.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import groovy.transform.ToString;
import jakarta.persistence.*;
import lombok.Data;
@Entity
@Table(name = "RoomPlayer")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString(excludes = {"room", "user"})
  // ✅ tránh vòng lặp vô hạn khi in đối tượng
public class RoomPlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    User user;
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false, referencedColumnName = "room_id")
    @JsonBackReference
    private Room room;
    private Integer score;
    private Boolean role;
}
