package com.api.Comics.helper;

import com.api.Comics.entities.ComicEntity;
import com.api.Comics.repository.ComicRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class CacheHelper {
    private final CacheManager cacheManager;
    private final ComicRepository comicRepository;

    @Autowired
    public CacheHelper(CacheManager cacheManager, ComicRepository comicRepository) {
        this.cacheManager = cacheManager;
        this.comicRepository = comicRepository;
    }

    public void loadComicsPageCache() {
        Cache comicsPageCache = cacheManager.getCache("comics-page-cache");
        if (comicsPageCache != null) {
            log.info("cache is present");
            comicsPageCache.invalidate();
            int pageNumber = 0;
            while (true) {
                log.info("Page number {}", pageNumber);
                Pageable comicPage = PageRequest.of(pageNumber, 500);
                Page<ComicEntity> resultsPage = comicRepository.findAllWithNotes(comicPage);
                if (resultsPage.isEmpty()) {
                    log.info("Page number {} is empty.", pageNumber);
                    break;
                } else {
                    log.info("Caching page {}", pageNumber);
                    comicsPageCache.put(pageNumber, resultsPage);
                }
                pageNumber++;

            }
        }
    }

    public Page<ComicEntity> getComicPage(int pageNumber) {
        Cache comicsPageCache = cacheManager.getCache("comics-page-cache");
        if (comicsPageCache != null) {
            Cache.ValueWrapper valueWrapper = comicsPageCache.get(pageNumber);
            if(valueWrapper != null) {
                ConcurrentHashMap<Integer, Page<ComicEntity>> nativeCache = (ConcurrentHashMap <Integer, Page<ComicEntity>>) comicsPageCache.getNativeCache();
                return nativeCache.get(pageNumber);
            }
            log.warn("Comic page #{} not found.", pageNumber);
        }
        return null;
    }
}
