package com.hanu.DailyFrame.controller;

import com.hanu.DailyFrame.models.Collection;
import com.hanu.DailyFrame.response.CollectionRespose;
import com.hanu.DailyFrame.service.CollectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/collections")
public class CollectionController {

    private final CollectionService collectionService;

    public CollectionController(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    @GetMapping
    public ResponseEntity<List<Collection>> getMyCollections() {
        return ResponseEntity.ok(collectionService.getMyCollections());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CollectionRespose> getCollectionById(@PathVariable Long id) {
        return ResponseEntity.ok(collectionService.getCollectionById(id));
    }
}
