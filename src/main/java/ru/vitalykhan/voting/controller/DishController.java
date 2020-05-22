package ru.vitalykhan.voting.controller;

import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import ru.vitalykhan.voting.model.Dish;
import ru.vitalykhan.voting.model.Menu;
import ru.vitalykhan.voting.repository.DishRepository;
import ru.vitalykhan.voting.repository.MenuRepository;
import ru.vitalykhan.voting.to.DishTo;
import ru.vitalykhan.voting.util.DishUtil;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;

import static ru.vitalykhan.voting.controller.MenuController.TODAYS_MENUS_CACHE_NAME;
import static ru.vitalykhan.voting.util.ValidationUtil.assureIdConsistency;
import static ru.vitalykhan.voting.util.ValidationUtil.checkIsEnabled;
import static ru.vitalykhan.voting.util.ValidationUtil.checkIsFound;
import static ru.vitalykhan.voting.util.ValidationUtil.checkIsNew;

@RestController
@RequestMapping(value = "/dishes", produces = MediaType.APPLICATION_JSON_VALUE)
public class DishController extends AbstractController {
    public final static String ENTITY_NAME = "dish";

    {
        log = LoggerFactory.getLogger(getClass());
    }

    private DishRepository dishRepository;

    public DishController(CacheManager cacheManager, MenuRepository menuRepository, DishRepository dishRepository) {
        super(cacheManager, menuRepository);
        this.dishRepository = dishRepository;
    }

    @GetMapping("/{dishId}")
    public Dish getById(@PathVariable int dishId) {
        log.info("Get dish with id={}", dishId);
        return dishRepository.findById(dishId).orElseThrow();
    }

    @DeleteMapping("/{dishId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = TODAYS_MENUS_CACHE_NAME, allEntries = true)
    public void deleteById(@PathVariable int dishId) {
        log.info("Delete menu with id={}", dishId);
        checkIsFound(dishRepository.delete(dishId) != 0);
    }

    @PatchMapping("/{dishId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void enable(@PathVariable int dishId, @RequestParam boolean enabled) {
        log.info("{} the dish with id {}", enabled ? "Enable" : "Disable", dishId);
        Dish dish = dishRepository.findById(dishId).orElseThrow();

        //Treat the case the dish is being enabled while its menu has been disabled
        if (enabled) {
            Integer menuId = dish.getMenu().getId();
            checkIsEnabled(menuRepository.findByEnabledTrueAndId(menuId) != null,
                    menuId, MenuController.ENTITY_NAME);
        }
        dish.setEnabled(enabled);
        dishRepository.save(dish);

        evictCacheIfTodays(dish.getMenu());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<Dish> create(@Valid @RequestBody DishTo dishTo) {
        checkIsNew(dishTo);

        int menuId = dishTo.getMenuId();
        Menu menu = menuRepository.findById(menuId).orElseThrow();
        checkIsEnabled(menu.isEnabled(), menuId, MenuController.ENTITY_NAME);

        Dish newDish = dishRepository.save(DishUtil.of(dishTo, menu));
        log.info("Create a new dish {}", newDish);

        evictCacheIfTodays(newDish.getMenu());

        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("dishes/{id}")
                .buildAndExpand(newDish.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(newDish);
    }

    @PutMapping(value = "/{dishId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@Valid @RequestBody DishTo dishTo, @PathVariable int dishId) {
        log.info("Update dish with id={}", dishId);
        assureIdConsistency(dishTo, dishId);
        Dish oldDish = dishRepository.findById(dishId).orElseThrow();

        int newMenuId = dishTo.getMenuId();
        Menu newMenu = menuRepository.findById(newMenuId).orElseThrow();
        checkIsEnabled(newMenu.isEnabled(), newMenuId, MenuController.ENTITY_NAME);

        Dish newDish = DishUtil.of(dishTo, newMenu);
        dishRepository.save(newDish);

        //Treat the case updating of the {menu id} affects today's menus
        //Assure no duplication of cache evicting
        if (!evictCacheIfTodays(newMenu)) {
            evictCacheIfTodays(oldDish.getMenu());
        }
    }

    private boolean evictCacheIfTodays(Menu newMenu) {
        if (newMenu.getDate().equals(LocalDate.now())) {
            evictCache();
            return true;
        }
        return false;
    }
}
