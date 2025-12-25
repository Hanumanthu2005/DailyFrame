package com.hanu.DailyFrame.service.imp;

import com.hanu.DailyFrame.models.Entry;
import com.hanu.DailyFrame.repo.EntryRepo;
import com.hanu.DailyFrame.service.EntryService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class EntryServiceImp implements EntryService {

    private final EntryRepo entryRepo;

    public EntryServiceImp(EntryRepo entryRepo) {
        this.entryRepo = entryRepo;
    }

    public Optional<Entry> getEntry(Long id) {
        return entryRepo.findById(id);
    }

    public Entry save(Entry entry) {
        return entryRepo.save(entry);
    }

    public Entry updateEntry(Entry updatedEntry) {

        Entry entry = entryRepo.findById(updatedEntry.getId())
                .orElseThrow(() -> new RuntimeException("Entry not found"));

        entry.setTitle(updatedEntry.getTitle());
        entry.setContent(updatedEntry.getContent());
        entry.setMediaList(updatedEntry.getMediaList());

        return entryRepo.save(entry);
    }

    public List<Entry> getByDate(LocalDate date) {
        LocalDateTime startDate = date.atStartOfDay();
        LocalDateTime endDate = date.atTime(LocalTime.MAX);
        return entryRepo.findByCreatedAtBetween(startDate, endDate);
    }

}
