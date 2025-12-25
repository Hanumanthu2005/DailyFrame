package com.hanu.DailyFrame.service.imp;

import com.hanu.DailyFrame.models.User;
import com.hanu.DailyFrame.repo.UserRepo;
import com.hanu.DailyFrame.request.LoginRequest;
import com.hanu.DailyFrame.request.SignupRequest;
import com.hanu.DailyFrame.response.LoginResponse;
import com.hanu.DailyFrame.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class UserServiceImp implements UserService {

    final UserRepo userRepo;
    final PasswordEncoder passwordEncoder;

    public UserServiceImp(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User signup(SignupRequest request) {
        if(userRepo.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already Exist");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(LocalDateTime.now());

        return userRepo.save(user);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email doesnot exist"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        LoginResponse response = new LoginResponse(user.getId(), user.getFullName(), user.getEmail());

        System.out.println(response.getFullName());
        return response;
    }
}
