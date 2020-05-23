package ru.vitalykhan.voting.controller;

import org.springframework.cache.CacheManager;
import ru.vitalykhan.voting.repository.MenuRepository;

import java.util.Objects;

import static ru.vitalykhan.voting.controller.MenuController.TODAYS_MENUS_CACHE_NAME;

public class AbstractController {
    protected CacheManager cacheManager;
    protected MenuRepository menuRepository;

    public AbstractController(CacheManager cacheManager, MenuRepository menuRepository) {
        this.cacheManager = cacheManager;
        this.menuRepository = menuRepository;
    }

    protected void evictCache() {
        Objects.requireNonNull(cacheManager.getCache(TODAYS_MENUS_CACHE_NAME)).clear();
    }
}
