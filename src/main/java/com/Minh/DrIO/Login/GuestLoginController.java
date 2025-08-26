package com.Minh.DrIO.Login;
import com.Minh.DrIO.DTO.Request.Response.GuestUserResponse;
import com.Minh.DrIO.model.GuestUser;
import com.Minh.DrIO.model.User;
import com.Minh.DrIO.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/guest-login")
public class GuestLoginController {
@Autowired
UserService service;

    @Operation(summary = "Đăng nhập ẩn danh", description = " trả về một ramdom UUID")
    @PostMapping("/")
    public ResponseEntity<?> creatguestlogin(@RequestParam String nickname) {
        try {
            GuestUser guestUser = service.createGuestLogin(nickname);
            GuestUserResponse GuestUserResponse = new GuestUserResponse(guestUser.getIdToken(), guestUser.getNickname());
            return ResponseEntity.ok(GuestUserResponse);
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }


}
