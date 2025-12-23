package com.hanu.DailyFrame.service;

import com.hanu.DailyFrame.models.Entry;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EntryService {

    Entry save(Entry entry);

    Optional<Entry> getEntry(Long id);

    Entry updateEntry(Entry entry);

    List<Entry> getByDate(LocalDate date);
}
