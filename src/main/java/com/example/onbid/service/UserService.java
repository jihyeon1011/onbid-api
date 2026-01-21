package com.example.onbid.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.onbid.dto.User;
import com.example.onbid.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    
    public boolean register(String username, String password) {
        if (userMapper.existsByUsername(username)) {
            return false; // 이미 존재하는 사용자
        }
        
        User user = new User();
        user.setUsername(username);
        
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        
        userMapper.insertUser(user);
        return true;
    }
    
    public boolean login(String username, String password) {
        User user = userMapper.findByUsername(username);
        
        if (user == null) return false;

        // 🔍 BCrypt로 비교
        return passwordEncoder.matches(password, user.getPassword());
    }
}