package com.hanu.DailyFrame.repo;

import com.hanu.DailyFrame.models.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepo extends JpaRepository<Media, Long> {
}
