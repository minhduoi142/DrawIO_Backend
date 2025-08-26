package com.Minh.DrIO.service;


import com.Minh.DrIO.Repository.UserRepository;
import com.Minh.DrIO.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RedisTemplate<String, Object> template;
    public String handleGoogleLogin(OAuth2AuthenticationToken authenticationToken) {
        Map<String, Object> attributes = authenticationToken.getPrincipal().getAttributes();
        String email = (String) attributes.get("email");
        String idToken = (String) authenticationToken.getCredentials();
        User user = userRepository.findByIdToken(idToken);
        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setIdToken(idToken);
            userRepository.save(user);
        }
        template.opsForHash().put("users:", idToken, user);
        return idToken;
    }
    public String validate(String idToken)
    {
        User user = (User) template.opsForHash().get("users:", idToken);
        Object storedToken = user.getIdToken().toString();
        if(storedToken != null)
        {
                return user.getNickname();
        }
        return null;
    }
    public String updateNickname(String idToken, String nickname) {
        User user = (User) template.opsForHash().get("users:", idToken);
        if (user == null) return "User not found";
        user.setNickname(nickname);
        template.opsForHash().put("users:", idToken, user);
        if (!user.isGuestuser()) {
            userRepository.save(user);
        }
        return nickname;
    }

}