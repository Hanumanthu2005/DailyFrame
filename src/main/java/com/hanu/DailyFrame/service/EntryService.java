package com.hanu.DailyFrame.service;

import com.hanu.DailyFrame.models.DairyEntries;
import com.hanu.DailyFrame.models.User;
import com.hanu.DailyFrame.repo.EntryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class EntryService {

    @Autowired
    EntryRepo entryRepo;

    public List<DairyEntries> getByUser(Long id) {
        return entryRepo.findByUserId(id);
    }
}
