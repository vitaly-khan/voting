package ru.vitalykhan.voting.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.vitalykhan.voting.model.Menu;
import ru.vitalykhan.voting.model.Restaurant;
import ru.vitalykhan.voting.repository.MenuRepository;
import ru.vitalykhan.voting.repository.RestaurantRepository;
import ru.vitalykhan.voting.to.MenuTo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.vitalykhan.voting.util.ValidationUtil.checkFound;
import static ru.vitalykhan.voting.util.ValidationUtil.checkIsNew;

@RestController
@RequestMapping(value = "/menus", produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private MenuRepository menuRepository;
    private RestaurantRepository restaurantRepository;

    public MenuController(MenuRepository menuRepository, RestaurantRepository restaurantRepository) {
        this.menuRepository = menuRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping("/{menuId}")
    public Menu getById(@PathVariable int menuId) {
        log.info("Get menu with id={}", menuId);
        Menu menu = menuRepository.findByIdWithRestaurantAndDishes(menuId);
        checkFound(menu != null, menuId, getClass());
        return menu;
    }

    @GetMapping
    public List<Menu> getByDate(@RequestParam @NotNull LocalDate date) {
        log.info("Get menus by date: {}", date);
        return menuRepository.findAllByDateOrderedByRestaurantName(date);
    }

    //The most popular method of API is to be cached!
    @GetMapping("/todays")
    @Cacheable("todaysMenus")
    public List<Menu> getTodays() {
        LocalDate now = LocalDate.now();
        log.info("Get today's ({}) menus", now);
        return menuRepository.findAllByDateOrderedByRestaurantName(now);
    }

    @DeleteMapping("/{menuId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "todaysMenus", allEntries = true)
    public void deleteByID(@PathVariable int menuId) {
        log.info("Delete menu with id={}", menuId);
        checkFound(menuRepository.delete(menuId) != 0, menuId, getClass());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @CacheEvict(value = "todaysMenus", allEntries = true)
    public ResponseEntity<Menu> create(@Valid @RequestBody MenuTo menuTo) {
        checkIsNew(menuTo);

        int restaurantId = menuTo.getRestaurantId();
        Restaurant restaurant = restaurantRepository.getOne(restaurantId);
        checkFound(restaurant != null, restaurantId, Restaurant.class);

        Menu newMenu = menuRepository.save(new Menu(menuTo.getDate(), restaurant));
        log.info("Create a new menu {}", newMenu);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("menus/{id}")
                .buildAndExpand(newMenu.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(newMenu);
    }
}
