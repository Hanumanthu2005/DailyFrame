package com.hanu.DailyFrame.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class ProfileResponse {

    private String fullName;

    private String email;

    private LocalDateTime createdAt;

}
