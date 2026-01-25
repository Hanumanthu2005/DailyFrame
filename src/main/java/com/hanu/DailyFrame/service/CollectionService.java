package com.hanu.DailyFrame.service;


import com.hanu.DailyFrame.models.Collection;
import com.hanu.DailyFrame.response.CollectionRespose;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface CollectionService {
    List<Collection> getMyCollections();

    CollectionRespose getCollectionById(Long id);
}
