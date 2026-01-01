package com.hanu.DailyFrame.service.imp;

import com.hanu.DailyFrame.models.Entry;
import com.hanu.DailyFrame.models.Media;
import com.hanu.DailyFrame.models.User;
import com.hanu.DailyFrame.repo.EntryRepo;
import com.hanu.DailyFrame.repo.MediaRepo;
import com.hanu.DailyFrame.repo.UserRepo;
import com.hanu.DailyFrame.request.EntryRequest;
import com.hanu.DailyFrame.service.EntryService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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

    public Entry getEntry(Long id) {
        User user = getCurrentUser();
        Entry entry = entryRepo.findById(id).orElseThrow(() -> new RuntimeException("Entry not found"));
        if(!user.getId().equals(entry.getUser().getId())) {
            throw new RuntimeException("invalid user");
        }
        return entry;
    }

    public void deleteEntry(Long id)  {
        User user = getCurrentUser();
        Entry entry = entryRepo.findById(id)
                        .orElseThrow(() -> new RuntimeException("invalid Entry id"));

        if(!entry.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        entryRepo.delete(entry);
    }

    public Entry save(EntryRequest entry) {
        User user = getCurrentUser();

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

    public Entry updateEntry(Long id, EntryRequest updatedEntry) {
        User user = getCurrentUser();

        Entry entry = entryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Entry not found"));

        if(!user.getId().equals(entry.getUser().getId())) {
            throw  new RuntimeException("invalid user");
        }

        entry.setTitle(updatedEntry.getTitle());
        entry.setContent(updatedEntry.getContent());
        entry.getMediaList().clear();

        if(updatedEntry.getMediaUrls() != null) {
            for(String url : updatedEntry.getMediaUrls()) {
                Media media = new Media();
                media.setUploadedAt(LocalDateTime.now());
                media.setFileUrl(url);
                media.setDairyEntry(entry);
                mediaRepo.save(media);
                entry.getMediaList().add(media);
            }
        }

        return entryRepo.save(entry);
    }

    public List<Entry> getByDate(LocalDate date) {
        LocalDateTime startDate = date.atStartOfDay();
        LocalDateTime endDate = date.atTime(LocalTime.MAX);
        return entryRepo.findByCreatedAtBetween(startDate, endDate);
    }

    public List<Entry> getByUser() {
        User user = getCurrentUser();
        return entryRepo.findByUserId(user.getId());
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("user not found"));
    }

}
