package com.hanu.DailyFrame.service;

import com.hanu.DailyFrame.models.User;
import com.hanu.DailyFrame.request.LoginRequest;
import com.hanu.DailyFrame.request.PasswordChange;
import com.hanu.DailyFrame.request.SignupRequest;
import com.hanu.DailyFrame.response.LoginResponse;
import com.hanu.DailyFrame.response.ProfileResponse;

public interface UserService {
    User signup(SignupRequest request);

    LoginResponse login(LoginRequest request);

    User changePassword(PasswordChange newPassword);

    ProfileResponse getProfile();
}
