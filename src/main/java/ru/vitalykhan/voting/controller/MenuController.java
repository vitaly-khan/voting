package ru.vitalykhan.voting.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.vitalykhan.voting.model.Menu;
import ru.vitalykhan.voting.model.Restaurant;
import ru.vitalykhan.voting.repository.DishRepository;
import ru.vitalykhan.voting.repository.MenuRepository;
import ru.vitalykhan.voting.repository.RestaurantRepository;
import ru.vitalykhan.voting.to.MenuTo;
import ru.vitalykhan.voting.util.MenuUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.vitalykhan.voting.util.ValidationUtil.assureIdConsistency;
import static ru.vitalykhan.voting.util.ValidationUtil.checkIsEnabled;
import static ru.vitalykhan.voting.util.ValidationUtil.checkIsFound;
import static ru.vitalykhan.voting.util.ValidationUtil.checkIsNew;
import static ru.vitalykhan.voting.util.ValidationUtil.checkIsPresentOrFuture;
import static ru.vitalykhan.voting.util.ValidationUtil.checkNestedEntityNotExist;

@RestController
@RequestMapping(value = "/menus", produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuController extends AbstractController {
    public final static String ENTITY_NAME = "menu";
    public static final String TODAYS_MENUS_CACHE_NAME = "todaysMenus";

    private final Logger log = LoggerFactory.getLogger(getClass());

    private RestaurantRepository restaurantRepository;
    private DishRepository dishRepository;

    public MenuController(CacheManager cacheManager, MenuRepository menuRepository, RestaurantRepository restaurantRepository, DishRepository dishRepository) {
        super(cacheManager, menuRepository);
        this.restaurantRepository = restaurantRepository;
        this.dishRepository = dishRepository;
    }

    @GetMapping("/{menuId}")
    public Menu getById(@PathVariable int menuId) {
        log.info("Get menu with id={}", menuId);
        Menu menu = menuRepository.findByIdWithRestaurantAndDishes(menuId);
        checkIsFound(menu != null);
        return menu;
    }

    @GetMapping("/history")
    public List<Menu> getByDate(@RequestParam @NotNull LocalDate date) {
        log.info("Get menus by date: {}", date);
        return menuRepository.findAllByDate(date);
    }

    @GetMapping
    public List<Menu> getEnabledByDate(@RequestParam @NotNull LocalDate date) {
        log.info("Get enabled menus by date: {}", date);
        return menuRepository.findAllEnabledByDate(date);
    }

    @GetMapping("/todays")
    @Cacheable(TODAYS_MENUS_CACHE_NAME)
    public List<Menu> getTodays() {
        LocalDate now = LocalDate.now();
        log.info("Get today's ({}) menus", now);
        return menuRepository.getTodays(now);
    }

    @DeleteMapping("/{menuId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable int menuId) {
        log.info("Delete menu with id={}", menuId);

        //Menu deletion is allowed only if menu has no dishes (otherwise disabling is a way to go)
        //Hence, no need for cache evicting
        checkNestedEntityNotExist(dishRepository.countAllByMenuId(menuId) == 0,
                menuId, ENTITY_NAME, DishController.ENTITY_NAME);

        checkIsFound(menuRepository.delete(menuId) != 0);
    }

    @PatchMapping("/{menuId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void enable(@PathVariable int menuId, @RequestParam boolean enabled) {
        log.info("{} the menu with id {}", enabled ? "Enable" : "Disable", menuId);
        Menu menu = menuRepository.findById(menuId).orElseThrow();

        //Treat the case the menu is being enabled while its restaurant has been disabled
        if (enabled) {
            Integer restaurantId = menu.getRestaurant().getId();
            checkIsEnabled(restaurantRepository.findByEnabledTrueAndId(restaurantId) != null,
                    restaurantId, RestaurantController.ENTITY_NAME);
        }
        menu.setEnabled(enabled);
        menuRepository.save(menu);

        if (!enabled) {
            menuRepository.cascadeDishDisabling(menuId);
            //Business logic implies no necessity to clear today's menu cache after ENABLING menu,
            //as its dishes are disabled (due to cascade disabling) or absent
            evictCacheIfTodays(menu);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<Menu> create(@Valid @RequestBody MenuTo menuTo) {
        checkIsNew(menuTo);

        int restaurantId = menuTo.getRestaurantId();
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow();
        checkIsEnabled(restaurant.isEnabled(), restaurantId, RestaurantController.ENTITY_NAME);

        Menu newMenu = menuRepository.save(MenuUtil.of(menuTo, restaurant));
        log.info("Create a new menu {}", newMenu);

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("menus/{id}")
                .buildAndExpand(newMenu.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(newMenu);

        //No need for today's menu cache evicting until menu contains at least one dish
    }

    @PutMapping(value = "/{menuId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@Valid @RequestBody MenuTo menuTo, @PathVariable int menuId) {
        log.info("Update menu with id={}", menuId);
        assureIdConsistency(menuTo, menuId);
        Menu oldMenu = menuRepository.findById(menuId).orElseThrow();
        checkIsPresentOrFuture(oldMenu);

        int restaurantId = menuTo.getRestaurantId();
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow();
        checkIsEnabled(restaurant.isEnabled(), restaurantId, RestaurantController.ENTITY_NAME);

        Menu newMenu = MenuUtil.of(menuTo, restaurant);
        menuRepository.save(newMenu);

        //Treat the case updating the date of the menu affects today's menus
        //Assure no duplication of cache evicting
        if (!evictCacheIfTodays(newMenu)) {
            evictCacheIfTodays(oldMenu);
        }
    }

    private boolean evictCacheIfTodays(Menu menu) {
        if (menu.getDate().equals(LocalDate.now()) && !menu.getDishes().isEmpty()) {
            evictTodaysMenusCache();
            return true;
        }
        return false;
    }

    //https://stackoverflow.com/questions/26147044/spring-cron-expression-for-every-day-101am
    //Cache evicting at midnight
    @Scheduled(cron = "0 0 0 * * *")
    void evictTodaysMenusCache() {
        log.info("Clear the cache of today's menus");
        evictCache();
    }
}
