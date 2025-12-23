package com.hanu.DailyFrame.controller;

import com.hanu.DailyFrame.models.Entry;
import com.hanu.DailyFrame.service.EntryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
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
    public ResponseEntity<Entry> getEntry(@PathVariable Long id) {
        Entry entry = entryService.getEntry(id)
                .orElseThrow(() -> new RuntimeException("Entry no found"));
        return ResponseEntity.ok(entry);
    }

    @PostMapping("/createEntry")
    public ResponseEntity<Entry> createEntry(@RequestBody Entry entry) {
        Entry savedEntry = entryService.save(entry);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEntry);
    }

    @PutMapping("/update/{id}")
public ResponseEntity<Entry> updateEntry(@PathVariable Long id, @RequestBody Entry entry) {
        entry.setId(id);
        Entry resultEntry = entryService.updateEntry(entry);
        return ResponseEntity.ok(resultEntry);
    }

    @GetMapping("/day")
    public ResponseEntity<List<Entry>> getByDate(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {
        List<Entry> result = entryService.getByDate(date);
        return ResponseEntity.ok(result);
    }
}
