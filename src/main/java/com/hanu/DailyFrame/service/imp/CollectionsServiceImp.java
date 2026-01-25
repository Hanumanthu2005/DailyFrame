package com.hanu.DailyFrame.service.imp;

import com.hanu.DailyFrame.models.Collection;
import com.hanu.DailyFrame.models.Entry;
import com.hanu.DailyFrame.models.User;
import com.hanu.DailyFrame.repo.CollectionRepo;
import com.hanu.DailyFrame.repo.UserRepo;
import com.hanu.DailyFrame.response.CollectionRespose;
import com.hanu.DailyFrame.response.EntryResponse;
import com.hanu.DailyFrame.service.CollectionService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CollectionsServiceImp implements CollectionService {


    private final CollectionRepo collectionRepo;
    private final UserRepo userRepo;

    public CollectionsServiceImp(CollectionRepo collectionRepo, UserRepo userRepo) {
        this.collectionRepo = collectionRepo;
        this.userRepo = userRepo;
    }

    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        return userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


    @Override
    public List<Collection> getMyCollections() {
        User user = getCurrentUser();
        return collectionRepo.findByUser(user);
    }

    @Override
    public CollectionRespose getCollectionById(Long id) {
        User user = getCurrentUser();

        Collection collection = collectionRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Collection not found"));

        if(!collection.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        CollectionRespose respose = new CollectionRespose();

        respose.setId(collection.getId());
        respose.setName(collection.getName());
        respose.setCreatedAt(collection.getCreatedAt());

        respose.setEntries(
                collection.getEntries().stream().map(entry -> {
                    EntryResponse res = new EntryResponse();
                    res.setId(entry.getId());
                    res.setTitle(entry.getTitle());
                    res.setContent(entry.getContent());
                    res.setCreatedAt(entry.getCreatedAt());

                    return res;
                }).toList()
        );

        return respose;
    }
}
