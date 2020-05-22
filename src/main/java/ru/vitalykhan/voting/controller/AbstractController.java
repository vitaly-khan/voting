package ru.vitalykhan.voting.controller;

import org.slf4j.Logger;
import org.springframework.cache.CacheManager;
import ru.vitalykhan.voting.repository.MenuRepository;

import java.util.Objects;

public class AbstractController {
    protected CacheManager cacheManager;
    protected MenuRepository menuRepository;
    protected Logger log;

    public AbstractController(CacheManager cacheManager, MenuRepository menuRepository) {
        this.cacheManager = cacheManager;
        this.menuRepository = menuRepository;
    }

    protected void evictCache() {
        log.info("Clear the cache of today's menus");
        Objects.requireNonNull(cacheManager.getCache("todaysMenus")).clear();
    }
}
