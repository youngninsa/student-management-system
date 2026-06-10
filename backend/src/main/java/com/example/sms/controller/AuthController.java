package com.example.sms.controller;

import com.example.sms.common.ApiResponse;
import com.example.sms.dto.LoginRequest;
import com.example.sms.dto.LoginResponse;
import com.example.sms.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // 简化登录：校验用户名和密码，返回用户基本信息，由前端保存登录状态。
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        return ApiResponse.success(authService.login(request));
    }
}
