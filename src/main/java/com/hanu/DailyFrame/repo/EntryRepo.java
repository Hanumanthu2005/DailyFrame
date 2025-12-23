package com.hanu.DailyFrame.repo;

import com.hanu.DailyFrame.models.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EntryRepo extends JpaRepository<Entry, Long> {

    List<Entry> findByUserId(Long id);

    Optional<Entry> findById(Long id);

    List<Entry> findByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime);
}
