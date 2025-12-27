package com.hanu.DailyFrame.service;

import com.hanu.DailyFrame.models.Entry;
import com.hanu.DailyFrame.request.EntryRequest;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EntryService {

    Entry save(EntryRequest entry, Authentication authentication);

    Optional<Entry> getEntry(Long id);

    Entry updateEntry(Entry entry);

    List<Entry> getByDate(LocalDate date);

    List<Entry> getByUser();
}
