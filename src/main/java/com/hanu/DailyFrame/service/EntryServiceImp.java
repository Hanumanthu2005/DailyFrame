package com.hanu.DailyFrame.service;

import com.hanu.DailyFrame.models.Entry;
import com.hanu.DailyFrame.repo.EntryRepo;
import org.springframework.stereotype.Service;

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

}
