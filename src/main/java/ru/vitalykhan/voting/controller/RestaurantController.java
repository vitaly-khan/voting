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
import ru.vitalykhan.voting.model.Restaurant;
import ru.vitalykhan.voting.repository.MenuRepository;
import ru.vitalykhan.voting.repository.RestaurantRepository;
import ru.vitalykhan.voting.util.ValidationUtil;

import javax.validation.Valid;
import java.net.URI;

import static ru.vitalykhan.voting.util.ValidationUtil.assureIdConsistency;
import static ru.vitalykhan.voting.util.ValidationUtil.checkIsFound;
import static ru.vitalykhan.voting.util.ValidationUtil.checkIsNew;

@RestController
@RequestMapping(value = "/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController extends AbstractController {
    public final static String ENTITY_NAME = "restaurant";

    {
        log = LoggerFactory.getLogger(getClass());
    }

    private RestaurantRepository restaurantRepository;

    public RestaurantController(CacheManager cacheManager, MenuRepository menuRepository, RestaurantRepository restaurantRepository) {
        super(cacheManager, menuRepository);
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping("/history")
    public Iterable<Restaurant> getAll() {
        log.info("Get all restaurants");
        return restaurantRepository.findAll();
    }

    @GetMapping
    public Iterable<Restaurant> getAllEnabled() {
        log.info("Get all enabled restaurants");
        return restaurantRepository.findByEnabledTrueOrderByName();
    }

    @GetMapping("/{restaurantId}")
    public Restaurant getById(@PathVariable int restaurantId) {
        log.info("Get restaurants with id={}", restaurantId);
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
        checkIsFound(restaurant != null, restaurantId, ENTITY_NAME);
        return restaurant;
    }

    @DeleteMapping("/{restaurantId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByID(@PathVariable int restaurantId) {
        log.info("Delete restaurant with id={}", restaurantId);

        //Restaurant deletion is allowed only if restaurant has no menus (otherwise disabling is a way to go)
        //Hence, no need for today's menu cache evicting
        ValidationUtil.checkNestedEntityNotExists(menuRepository.countAllByIdRestaurant(restaurantId) == 0,
                restaurantId, ENTITY_NAME, MenuController.ENTITY_NAME);
        checkIsFound(restaurantRepository.delete(restaurantId) != 0, restaurantId, ENTITY_NAME);
    }

    @PatchMapping("/{restaurantId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void enable(@PathVariable int restaurantId, @RequestParam boolean enabled) {
        log.info("{} the restaurant with id {}", enabled ? "Enable" : "Disable", restaurantId);
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
        checkIsFound(restaurant != null, restaurantId, ENTITY_NAME);

        restaurant.setEnabled(enabled);
        restaurantRepository.save(restaurant);

        if (!enabled) {
            restaurantRepository.cascadeMenuDisabling(restaurantId);
            restaurantRepository.cascadeDishDisabling(restaurantId);
            //Business logic implies no necessity to clear today's menu cache after ENABLING restaurant,
            //as its menus are disabled (due to cascade disabling) or absent
            super.evictCache();
        }
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> create(@Valid @RequestBody Restaurant restaurant) {
        checkIsNew(restaurant);

        Restaurant newRestaurant = restaurantRepository.save(restaurant);
        log.info("Create a new restaurant {}", newRestaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("restaurants/{id}")
                .buildAndExpand(newRestaurant.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(newRestaurant);
    }

    @PutMapping(value = "/{restaurantId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Transactional
    @CacheEvict(value = "todaysMenus", allEntries = true)
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int restaurantId) {
        log.info("Update restaurant with id={}", restaurantId);
        assureIdConsistency(restaurant, restaurantId);
        checkIsFound(restaurantRepository.existsById(restaurantId), restaurantId, ENTITY_NAME);

        restaurantRepository.save(restaurant);
    }
}
