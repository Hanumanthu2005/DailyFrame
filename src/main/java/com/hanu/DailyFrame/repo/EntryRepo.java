package com.hanu.DailyFrame.repo;

import com.hanu.DailyFrame.models.DairyEntries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EntryRepo extends JpaRepository<DairyEntries, Long> {

    List<DairyEntries> findByUserId(Long id);
}
