package com.Minh.DrIO.Repository;


import com.Minh.DrIO.model.Room;
import com.Minh.DrIO.model.RoomPlayer;
import com.Minh.DrIO.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import java.util.List;

@Repository
public interface RoomPlayerRepository extends JpaRepository<RoomPlayer, Integer> {
    List<RoomPlayer> findByRoomId(int roomId);
    RoomPlayer findByUser(User user);
    // RoomPlayerRepository.java
Optional<RoomPlayer> findByRoomAndUser(Room room, User user);

}
