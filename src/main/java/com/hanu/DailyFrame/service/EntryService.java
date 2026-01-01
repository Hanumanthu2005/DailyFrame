package com.hanu.DailyFrame.service;

import com.hanu.DailyFrame.models.Entry;
import com.hanu.DailyFrame.request.EntryRequest;
import java.time.LocalDate;
import java.util.List;

public interface EntryService {

    Entry save(EntryRequest entry);

    Entry getEntry(Long id);

    Entry updateEntry(Long id, EntryRequest entry);

    List<Entry> getByDate(LocalDate date);

    List<Entry> getByUser();

    void deleteEntry(Long id);
}
