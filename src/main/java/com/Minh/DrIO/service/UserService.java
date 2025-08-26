package com.Minh.DrIO.service;


import com.Minh.DrIO.Repository.UserRepository;
import com.Minh.DrIO.model.GuestUser;
import com.Minh.DrIO.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    RedisTemplate<String,Object> generalRedisTemplate;
    @Autowired
    UserRepository userRepository;
//lưu lâu dài 1 user mới vào database
    Random random = new Random();
    public User createUser(User user)
    {
        return userRepository.save(user);
    }
    public GuestUser createGuestLogin(String nickname) {
        try {
            // Tạo một ID khách mới
            String guestid = UUID.randomUUID().toString();
            // Tạo đối tượng GuestUser và User
            GuestUser guestUser = new GuestUser();
            guestUser.setNickname(nickname);
            guestUser.setIdToken(guestid);
            User user = new User();
            user.setNickname(nickname);
            user.setGuestuser(true);
            userRepository.save(user); // Lưu User vào cơ sở dữ liệu
            // Lưu GuestUser vào Redis
            generalRedisTemplate.opsForHash().put("users:", guestid, user);
            return guestUser;
        } catch (Exception e) {
            // In ra thông tin lỗi để hỗ trợ debug
            e.printStackTrace();
            throw new RuntimeException("Error creating guest user", e);  // Thực hiện ném ngoại lệ để gửi phản hồi rõ ràng
        }
    }
//    public User findUser(User user)
//    {
//        return userRepository.findByEmail(user.getEmail());
//    }
}
