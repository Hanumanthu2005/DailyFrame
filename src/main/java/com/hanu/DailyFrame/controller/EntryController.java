package com.hanu.DailyFrame.controller;

import com.hanu.DailyFrame.models.DairyEntries;
import com.hanu.DailyFrame.models.User;
import com.hanu.DailyFrame.service.EntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EntryController {

    @Autowired
    EntryService entryService;

    @GetMapping("/{id}")
    public ResponseEntity<List<DairyEntries>> getAll(@PathVariable Long id) {
        List<DairyEntries> entry = entryService.getByUser(id);
        return ResponseEntity.ok(entry);
    }
}
