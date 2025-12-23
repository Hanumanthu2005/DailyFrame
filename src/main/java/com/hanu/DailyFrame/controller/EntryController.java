package com.hanu.DailyFrame.controller;

import com.hanu.DailyFrame.models.Entry;
import com.hanu.DailyFrame.service.EntryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/entry")
public class EntryController {

    private final EntryService entryService;

    public EntryController(EntryService entryService)
    {
        this.entryService = entryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Entry>> getEntry(@PathVariable Long id) {
        Optional<Entry> entry = entryService.getEntry(id);
        return ResponseEntity.ok(entry);
    }

    @PostMapping("/createEntry")
    public ResponseEntity<?> createEntry(@RequestBody Entry entry) {
        Entry savedEntry = entryService.save(entry);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEntry);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Entry> updateEntry(@RequestBody Entry entry) {
        Entry resultEntry = entryService.updateEntry(entry);
        return ResponseEntity.ok(resultEntry);
    }
}
