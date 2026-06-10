package com.example.sms.service;

import com.example.sms.dto.LoginRequest;
import com.example.sms.dto.LoginResponse;
import com.example.sms.entity.User;
import com.example.sms.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("用户名或密码错误"));
        if (!user.getPassword().equals(request.getPassword())) {
            throw new IllegalArgumentException("用户名或密码错误");
        }
        return new LoginResponse(user.getId(), user.getUsername(), user.getRole(), user.getRealName(), user.getStudentId());
    }
}
