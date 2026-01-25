package com.hanu.DailyFrame.repo;

import com.hanu.DailyFrame.models.Collection;
import com.hanu.DailyFrame.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollectionRepo extends JpaRepository<Collection, Long> {

    List<Collection> findByUser(User user);
    
    Optional<Collection> findByUserAndName(User user, String personal);
}
