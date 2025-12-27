package com.hanu.DailyFrame.service.imp;

import com.hanu.DailyFrame.models.Entry;
import com.hanu.DailyFrame.models.Media;
import com.hanu.DailyFrame.models.User;
import com.hanu.DailyFrame.repo.EntryRepo;
import com.hanu.DailyFrame.repo.MediaRepo;
import com.hanu.DailyFrame.repo.UserRepo;
import com.hanu.DailyFrame.request.EntryRequest;
import com.hanu.DailyFrame.service.EntryService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class EntryServiceImp implements EntryService {

    private final EntryRepo entryRepo;
    private final UserRepo userRepo;
    private final MediaRepo mediaRepo;

    public EntryServiceImp(EntryRepo entryRepo, UserRepo userRepo, MediaRepo mediaRepo) {
        this.entryRepo = entryRepo;
        this.userRepo = userRepo;
        this.mediaRepo = mediaRepo;
    }

    public Optional<Entry> getEntry(Long id) {
        return entryRepo.findById(id);
    }

    public Entry save(EntryRequest entry, Authentication authentication) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("user not found"));

        Entry res = new Entry();

        res.setTitle(entry.getTitle());
        res.setContent(entry.getContent());
        res.setCreatedAt(LocalDateTime.now());
        res.setUser(user);

        Entry savedEntry = entryRepo.save(res);

        if(entry.getMediaUrls() != null && !entry.getMediaUrls().isEmpty()) {
            List<Media> mediaList = entry.getMediaUrls().stream()
                    .map(url -> {
                        Media media = new Media();
                        media.setFileUrl(url);
                        media.setUploadedAt(LocalDateTime.now());
                        media.setDairyEntry(savedEntry);
                        return media;
                            })
                    .toList();
            mediaRepo.saveAll(mediaList);

        }

        return savedEntry;
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

    public List<Entry> getByUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid user"));

        return entryRepo.findByUserId(user.getId());
    }

}
