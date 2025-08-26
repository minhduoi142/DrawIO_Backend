package com.Minh.DrIO.service;


import com.Minh.DrIO.DTO.Request.Response.RoomCreateRequest;
import com.Minh.DrIO.DTO.Request.Response.RoomCreateResponse;
import com.Minh.DrIO.Repository.RoomPlayerRepository;
import com.Minh.DrIO.Repository.RoomRepository;
import com.Minh.DrIO.Repository.UserRepository;
import com.Minh.DrIO.model.Room;
import com.Minh.DrIO.model.RoomPlayer;
import com.Minh.DrIO.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.ObjectInputFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoomService {
    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private RoomPlayerRepository roomPlayerRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    RedisTemplate<String,Object> generalRedisTemplate;
    public String shortUUID(int length)
    {
        String code = UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0,length );
        return code;
    }

    public String createRoom(Room room) {
       String code;
        do {
             code = shortUUID(6);
        }
        while(roomRepository.existsByCodeRoom(code));
        room.setCodeRoom(code);
        System.out.println(room.getCodeRoom());
        RoomCreateResponse roomCreateResponse = new RoomCreateResponse(
                room.getMaxPlayer(),
                room.getTopic(),
                room.isPrivate(),
                room.getTargetPoint(),
                room.getCodeRoom(),
                room.getLang()
        );
       roomRepository.save(room);
       String inviteLink = "https://dev-api.drawio.cdtm-ito.org/api/rooms/invite/" + room.getCodeRoom();
       // redisTemplate.opsForHash().put("room:" + savedRoom.getId(), "status", "waiting");
        return inviteLink ;
    }

    public Room joinRoom(Integer roomId, String id) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalStateException("Room not found"));
        if (room.getMaxPlayer() <= roomPlayerRepository.findByRoomId(roomId).size()) {
            throw new IllegalStateException("Room is full");
        }
        Object data = redisTemplate.opsForHash().get("users:", id);
        if (data == null) {
            throw new IllegalStateException("User not found in session");
        }

        User user = objectMapper.convertValue(data, User.class);

        user = userRepository.save(user); // Save vào DB để tránh lỗi transient

        RoomPlayer roomPlayer = new RoomPlayer();
        roomPlayer.setRoom(room);
        roomPlayer.setUser(user);
        roomPlayer.setScore(0);
        roomPlayer.setRole(false);

        redisTemplate.opsForSet().add("roomid:" + roomId + ":current", roomPlayer);
        roomPlayerRepository.save(roomPlayer);

        redisTemplate.opsForHash().put("room:" + roomId, "players",
                roomPlayerRepository.findByRoomId(roomId).stream()
                        .map(rp -> rp.getUser().getNickname())
                        .toList());

        redisTemplate.convertAndSend(
                "room:" + roomId + ":updates",
                "{\"event\": \"player_joined\", \"username\": \"" + user.getNickname() + "\"}"
        );
        room.setTotalPlayers(room.getRoomPlayers().size());
        return room;
    }

    public List<RoomPlayer> getRoomPlayers(Integer roomId) {
        return roomPlayerRepository.findByRoomId(roomId);
    }

    public List<Room> getrooms() {
        List<Room> rooms = roomRepository.findAll();
        List<Room> publicRoom = new ArrayList<>();
        for( Room r : rooms)
        {
            if(!r.isPrivate())
            {
                publicRoom.add(r);
            }
        }
        return publicRoom;
    }

    public Integer avalibleRoom() {
        int min = Integer.MAX_VALUE;
        int id = 0;
        List<Room> rooms = roomRepository.findAll();
        List<Room> publicRoom = new ArrayList<>();
        for( Room r : rooms)
        {
            if(! r.isPrivate())
                publicRoom.add(r);
        }
        for (Room r : publicRoom) {
            List<RoomPlayer> players = roomPlayerRepository.findByRoomId(r.getId());
            if (players.size() < r.getMaxPlayer() && players.size() < min) {
                min = players.size();
                id = r.getId();
            }
        }
        return id;
    }
    public Optional<Room> getRandomRoom(String idToken) {
        int id = avalibleRoom();
        Object data = generalRedisTemplate.opsForHash().get("users:", idToken);
        User user = objectMapper.convertValue(data, User.class);
        user = userRepository.save(user);
        if (id != 0) {
            Room room = roomRepository.findById(id).get();
            RoomPlayer roomPlayer = new RoomPlayer();
            roomPlayer.setRoom(room);
            roomPlayer.setUser(user);
            roomPlayer.setScore(0);
            roomPlayer.setRole(false);

            generalRedisTemplate.opsForSet().add("roomid:" + id + ":current", roomPlayer);
            roomPlayerRepository.save(roomPlayer);

            generalRedisTemplate.opsForHash().put("room:" + id, "players",
                    roomPlayerRepository.findByRoomId(id).stream()
                            .map(rp -> rp.getUser().getNickname())
                            .toList());
            generalRedisTemplate.convertAndSend(
                    "room:" + id + ":updates",
                    "{\"event\": \"player_joined\", \"username\": \"" + user.getNickname() + "\"}"
            );
            room.setTotalPlayers(room.getRoomPlayers().size());
            return roomRepository.findById(id);
        }
        return Optional.empty();
    }
    public Room delete(Integer roomid) {
        Room room = roomRepository.findById(roomid)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + roomid));
        roomRepository.deleteById(roomid);
        return room;
    }

    public Optional<Room> getRoom(int roomid) {
        return roomRepository.findById(roomid);
    }
    public Room joinInviteLink(String codeRoom)
    {
        Room room = roomRepository.findByCodeRoom(codeRoom);
        System.out.println(room.getId());
        return room;

    };
    @Transactional
public void exitRoom(int roomid, String idtoken) {
    Room room = roomRepository.findById(roomid)
        .orElseThrow(() -> new IllegalStateException("Room not found"));

    Object data = generalRedisTemplate.opsForHash().get("users:", idtoken);
    if (data == null) {
        throw new IllegalStateException("User not found in session");
    }
    User user = objectMapper.convertValue(data, User.class);

    user = userRepository.findById(user.getId())
        .orElseThrow(() -> new IllegalStateException("User not found in DB"));

    RoomPlayer roomPlayer = roomPlayerRepository.findByUser(user);
    System.out.println("RoomPlayer: " + roomPlayer.getId());

    if (roomPlayer != null && room.getRoomPlayers() != null) {
        // 1. Xóa RoomPlayer khỏi Room
        room.getRoomPlayers().remove(roomPlayer);

        // 2. Xóa RoomPlayer trong DB
        user.setRoomPlayer(null);
        userRepository.save(user);
        roomPlayerRepository.delete(roomPlayer);

        // 3. Xóa User
        
        // userRepository.delete(user);

        // 4. Cập nhật Redis
        generalRedisTemplate.opsForSet().remove("roomid:" + roomid + ":current", roomPlayer);
        generalRedisTemplate.opsForHash().delete("room:" + roomid, "players");

        // 5. Cập nhật lại số lượng player
        room.setTotalPlayers(room.getRoomPlayers().size());
        roomRepository.save(room);
    } else {
        throw new IllegalStateException("RoomPlayer not found");
    }
}
}
