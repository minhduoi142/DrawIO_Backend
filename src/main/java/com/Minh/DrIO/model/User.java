    package com.Minh.DrIO.model;

    import com.fasterxml.jackson.annotation.JsonIgnore;
    import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import groovy.transform.ToString;
import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Entity
    @Table(name = "users")
    @JsonIgnoreProperties(ignoreUnknown = true)
    @ToString(excludes = "roomPlayer") // ✅ tránh vòng lặp vô hạn khi in đối tượng 
    public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
            @Column(name = "user_id")
        private int id;
        private String idToken;
        private String email;
        private String nickname;
    //  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
   @OneToOne(mappedBy = "user")
        @JoinColumn(name = "room_player_id")
@JsonIgnore
        private RoomPlayer roomPlayer;
        private boolean guestuser;
    }
