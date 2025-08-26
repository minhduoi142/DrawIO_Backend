package com.Minh.DrIO.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Minh.DrIO.DTO.Request.Response.RoomCreateRequest;
import com.Minh.DrIO.DTO.Request.Response.RoomResponse;
import com.Minh.DrIO.model.Room;
import com.Minh.DrIO.service.RoomService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
    @RequestMapping("/api/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;
    @Operation(summary = "Hiển thị tất cả phòng chơi", description = "Trả về list phòng chơi")
    @GetMapping("/rooms")
    public ResponseEntity<List<Room>> getrooms()
    {
        return new ResponseEntity<>(roomService.getrooms(), HttpStatus.ACCEPTED);
    }
    @Operation(summary = "Tạo một phòng chơi mới")
    @PostMapping
    public ResponseEntity<?> createRoom(@RequestBody RoomCreateRequest dto) {
        try {
            Room room = new Room();
            room.setTargetPoint(dto.getTargetPoint());
            room.setTopic(dto.getTopic());
            room.setLang(dto.getLang());
            room.setMaxPlayer(dto.getMaxPlayer());
            room.setPrivate(dto.isPrivate());
            room.setTotalPlayers(0);
            return ResponseEntity.ok(roomService.createRoom(room));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Lỗi: " + e.getMessage());
        }
    }
    @Operation(summary = "Hiển thị tất cả người chơi trong 1 phòng")
    @GetMapping("/{roomId}")
    public ResponseEntity<?> getRoomPlayers(@PathVariable Integer roomId) {
        try {
            return ResponseEntity.ok(roomService.getRoomPlayers(roomId));
        }
    catch (Exception e)
    {
        return ResponseEntity.internalServerError().body(e.getMessage());
    }
    }
    @Operation(summary = "Tham gia một phòng chơi")
    @PostMapping("/{roomId}/join")
    public ResponseEntity<?> joinRoom(@PathVariable Integer roomId, @RequestParam String id) {
        try {
            Room room = roomService.joinRoom(roomId, id);
            RoomResponse response = new RoomResponse(room.getId(), room.getTopic(), room.getTotalPlayers(),room.getMaxPlayer(), room.getTargetPoint(), room.getLang());
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi không xác định: " + e.getMessage());
        }
    }
    @Operation(summary = "Join random room")
    @GetMapping("/getrandom-room")
    public ResponseEntity<?> joinRandomRoom(String idToken) {
        Optional<Room> room = roomService.getRandomRoom(idToken);
        if (room.isPresent()) {
            RoomResponse response = new RoomResponse(room.get().getId(), room.get().getTopic(), room.get().getTotalPlayers(),room.get().getMaxPlayer(), room.get().getTargetPoint(), room.get().getLang());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No available room");
        }
    }
    @DeleteMapping("/delete-room")
    public ResponseEntity<?> deleteRoom(Integer roomid)
    {
        try
        {
            Room room = roomService.delete(roomid);
            RoomResponse response = new RoomResponse(room.getId(), room.getTopic(), room.getTotalPlayers(),room.getMaxPlayer(), room.getTargetPoint(), room.getLang());
            return ResponseEntity.ok(response);
        }
        catch (Exception e)
        {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @Operation(summary = "get 1 room")
    @GetMapping("/room/{roomid}")
    public ResponseEntity<?> getRoom(@PathVariable int roomid)
    {
        try{
            Optional<Room> room = roomService.getRoom(roomid);
            if(room.isPresent())
            {
                RoomResponse response = new RoomResponse(room.get().getId(), room.get().getTopic(), room.get().getTotalPlayers(),room.get().getMaxPlayer(), room.get().getTargetPoint(), room.get().getLang());
                return ResponseEntity.ok(response);
            }
            else return ResponseEntity.status(404).body("NOT FOUND");
        }
        catch (Exception e)
        {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @Operation(summary = "Join by inviteLink")
    @GetMapping("/invite/{codeRoom}")
    public ResponseEntity<?> joinInviteLink(@PathVariable String codeRoom)
    {
        try
        {
            return ResponseEntity.ok(roomService.joinInviteLink(codeRoom));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return  ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
// ...existing code...

@Operation(summary = "Thoát phòng chơi")
@DeleteMapping("/{roomId}/exit")
public ResponseEntity<?> exitRoom(@PathVariable Integer roomId, @RequestParam String token) {
    try {
        roomService.exitRoom(roomId, token);
        return ResponseEntity.ok("succeed");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("failed: " + e.getMessage());
    }
}

// ...existing code...

}