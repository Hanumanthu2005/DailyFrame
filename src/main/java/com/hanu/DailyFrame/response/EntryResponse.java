package com.hanu.DailyFrame.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EntryResponse {

    private Long id;

    private String title;

    private String content;

    private LocalDateTime createdAt;
}
