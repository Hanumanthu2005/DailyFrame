package com.hanu.DailyFrame.controller;

import com.hanu.DailyFrame.models.User;
import com.hanu.DailyFrame.request.LoginRequest;
import com.hanu.DailyFrame.request.SignupRequest;
import com.hanu.DailyFrame.response.LoginResponse;
import com.hanu.DailyFrame.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        User user = userService.signup(request);
        return ResponseEntity.ok("user created successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }
}
