package com.Minh.DrIO.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Minh.DrIO.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("oauth/login")
public class AuthController {
@Autowired
AuthService service;
    @Operation(summary = "Chuyển hướng đăng nhập bằng Google")
    @CrossOrigin(origins = "*")
    @GetMapping("/google")
    public ResponseEntity<String> login(OAuth2AuthenticationToken authenticationToken)
    {
        String idToken = service.handleGoogleLogin(authenticationToken);
        if(idToken != null)
            return new ResponseEntity<>(idToken, HttpStatus.OK);
        else return new ResponseEntity<>("login failed", HttpStatus.BAD_REQUEST);
    }
    @Operation(summary = "cập nhật tên User trong game")
    @PutMapping("/update-nickname")
    public ResponseEntity<String> updateNickname(@RequestParam String idToken, @RequestParam String nickName)
    {
        return new ResponseEntity<>(service.updateNickname(idToken, nickName), HttpStatus.OK);
    }
    @Operation(summary = "xác thực idToken, email User")
    @GetMapping("/validate")
    public ResponseEntity<String> validate(String idToken, String email)
    {
   String idStored = service.validate(idToken);
   if( idStored != null)
        return new ResponseEntity<>(service.validate(idToken), HttpStatus.OK);
   else return new ResponseEntity<>("failed", HttpStatus.BAD_REQUEST);
    }
}
