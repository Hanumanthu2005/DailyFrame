package com.hanu.DailyFrame.controller;

import com.hanu.DailyFrame.models.Entry;
import com.hanu.DailyFrame.request.EntryRequest;
import com.hanu.DailyFrame.service.EntryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/entry")
public class EntryController {

    private final EntryService entryService;

    public EntryController(EntryService entryService)
    {
        this.entryService = entryService;
    }

    @GetMapping("/my")
    public ResponseEntity<List<Entry>> getByUser() {
        return ResponseEntity.ok(entryService.getByUser());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Entry> getEntry(@PathVariable Long id) {
        Entry entry = entryService.getEntry(id);
        return ResponseEntity.ok(entry);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createEntry(@RequestBody EntryRequest entry) {
        Entry savedEntry = entryService.save(entry);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEntry);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Entry> updateEntry(@PathVariable Long id, @RequestBody EntryRequest entry) {
        Entry resultEntry = entryService.updateEntry(id, entry);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntry(@PathVariable Long id) {
        entryService.deleteEntry(id);
        return ResponseEntity.noContent().build();
    }
}
