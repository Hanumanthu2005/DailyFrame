package com.hanu.DailyFrame.service;

import com.hanu.DailyFrame.models.Entry;

import java.util.Optional;

public interface EntryService {

    Entry save(Entry entry);

    Optional<Entry> getEntry(Long id);

    Entry updateEntry(Entry entry);
}
