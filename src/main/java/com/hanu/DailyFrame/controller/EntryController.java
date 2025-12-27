package com.hanu.DailyFrame.controller;

import com.hanu.DailyFrame.models.Entry;
import com.hanu.DailyFrame.models.User;
import com.hanu.DailyFrame.request.EntryRequest;
import com.hanu.DailyFrame.service.EntryService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.Authenticator;
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
        Entry entry = entryService.getEntry(id)
                .orElseThrow(() -> new RuntimeException("Entry no found"));
        return ResponseEntity.ok(entry);
    }

    @PostMapping("/createEntry")
    public ResponseEntity<?> createEntry(@RequestBody EntryRequest entry, Authentication authentication) {
        Entry savedEntry = entryService.save(entry, authentication);
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
