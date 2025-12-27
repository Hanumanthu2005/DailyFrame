package com.hanu.DailyFrame.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EntryRequest {

    private String title;

    private String content;

    private List<String> mediaUrls;
}
