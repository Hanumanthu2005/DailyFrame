package com.hanu.DailyFrame.service.imp;

import com.hanu.DailyFrame.models.*;
import com.hanu.DailyFrame.repo.*;
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
    private final CollectionRepo collectionRepo;

    public EntryServiceImp(
            EntryRepo entryRepo,
            UserRepo userRepo,
            MediaRepo mediaRepo,
            CollectionRepo collectionRepo
    ) {
        this.entryRepo = entryRepo;
        this.userRepo = userRepo;
        this.mediaRepo = mediaRepo;
        this.collectionRepo = collectionRepo;
    }

    @Override
    public Entry save(EntryRequest request) {
        User user = getCurrentUser();

        Entry entry = new Entry();
        entry.setTitle(request.getTitle());
        entry.setContent(request.getContent());
        entry.setUser(user);
        entry.setCollection(resolveCollection(request, user));

        Entry savedEntry = entryRepo.save(entry);

        saveMedia(request, savedEntry);
        return savedEntry;
    }

    @Override
    public Entry updateEntry(Long id, EntryRequest request) {
        User user = getCurrentUser();

        Entry entry = entryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Entry not found"));

        if (!entry.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        entry.setTitle(request.getTitle());
        entry.setContent(request.getContent());
        entry.setCollection(resolveCollection(request, user));

        entry.getMediaList().clear(); // orphanRemoval handles DB cleanup
        saveMedia(request, entry);

        return entryRepo.save(entry);
    }

    @Override
    public void deleteEntry(Long id) {
        User user = getCurrentUser();

        Entry entry = entryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Entry not found"));

        if (!entry.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        entryRepo.delete(entry);
    }

    @Override
    public Entry getEntry(Long id) {
        User user = getCurrentUser();

        Entry entry = entryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Entry not found"));

        if (!entry.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        return entry;
    }

    @Override
    public List<Entry> getByUser() {
        User user = getCurrentUser();
        return entryRepo.findByUserId(user.getId());
    }

    @Override
    public List<Entry> getByDate(LocalDate date) {
        return entryRepo.findByCreatedAtBetween(
                date.atStartOfDay(),
                date.atTime(LocalTime.MAX)
        );
    }

    // -------------------- HELPERS --------------------

    private Collection resolveCollection(EntryRequest request, User user) {

        String newName = request.getNewCollectionName();

        if (request.getCollectionId() != null &&
                newName != null &&
                !newName.isBlank()) {
            throw new IllegalArgumentException(
                    "Provide either collectionId or newCollectionName"
            );
        }

        if (request.getCollectionId() != null) {
            Collection collection = collectionRepo.findById(request.getCollectionId())
                    .orElseThrow(() -> new IllegalArgumentException("Collection not found"));

            if (!collection.getUser().getId().equals(user.getId())) {
                throw new IllegalArgumentException("Unauthorized");
            }
            return collection;
        }

        if (newName != null && !newName.isBlank()) {
            Collection collection = new Collection();
            collection.setName(newName.trim());
            collection.setUser(user);
            return collectionRepo.save(collection);
        }

        return getDefaultCollection(user);
    }


    private Collection getDefaultCollection(User user) {
        return collectionRepo.findByUserAndName(user, "Personal")
                .orElseGet(() -> {
                    Collection c = new Collection();
                    c.setName("Personal");
                    c.setUser(user);
                    return collectionRepo.save(c);
                });
    }

    private void saveMedia(EntryRequest request, Entry entry) {
        if (request.getMediaUrls() == null || request.getMediaUrls().isEmpty()) return;

        for (String url : request.getMediaUrls()) {
            Media media = new Media();
            media.setFileUrl(url);
            media.setUploadedAt(LocalDateTime.now());
            media.setDairyEntry(entry);
            entry.getMediaList().add(media);
        }
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
