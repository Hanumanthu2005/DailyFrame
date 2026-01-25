package com.hanu.DailyFrame.response;

import com.hanu.DailyFrame.models.Entry;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CollectionRespose {

    private Long id;

    private String name;

    private LocalDateTime createdAt;

    private List<EntryResponse> entries;

}
