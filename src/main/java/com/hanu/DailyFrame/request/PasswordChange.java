package com.hanu.DailyFrame.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChange {

    private String oldPassword;

    private String newPassword;

    private String confirmPassword;
}
