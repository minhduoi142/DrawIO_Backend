package com.Minh.DrIO.Repository;
import com.Minh.DrIO.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

    boolean existsByCodeRoom(String codeRoom);
    Room findByCodeRoom(String codeRoom);
}

