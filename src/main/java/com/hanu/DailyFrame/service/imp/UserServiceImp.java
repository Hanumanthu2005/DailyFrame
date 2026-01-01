package com.hanu.DailyFrame.service.imp;

import com.hanu.DailyFrame.models.User;
import com.hanu.DailyFrame.repo.UserRepo;
import com.hanu.DailyFrame.request.LoginRequest;
import com.hanu.DailyFrame.request.PasswordChange;
import com.hanu.DailyFrame.request.SignupRequest;
import com.hanu.DailyFrame.response.LoginResponse;
import com.hanu.DailyFrame.response.ProfileResponse;
import com.hanu.DailyFrame.security.JwtUtil;
import com.hanu.DailyFrame.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class UserServiceImp implements UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserServiceImp(UserRepo userRepo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
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
                .orElseThrow(() -> new RuntimeException("Email not found"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return new LoginResponse(token, user.getEmail());
    }

    @Override
    public User changePassword(PasswordChange newPassword) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));


        if(passwordEncoder.matches(newPassword.getOldPassword(), user.getPassword())) {
            if(Objects.equals(newPassword.getOldPassword(), newPassword.getNewPassword())) {
                throw new RuntimeException("New password must not be the old password");
            }

            if(!Objects.equals(newPassword.getNewPassword(), newPassword.getConfirmPassword())) {
                throw new RuntimeException("Confirmation password was different from the new password");
            }

            user.setPassword(passwordEncoder.encode(newPassword.getNewPassword()));

        }
        return userRepo.save(user);
    }

    public ProfileResponse getProfile() {
        User user = getCurrentUser();
        System.out.println("in auth service");
        ProfileResponse response = new ProfileResponse();

        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        response.setCreatedAt(user.getCreatedAt());

        return response;
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("user not found"));
    }
}
