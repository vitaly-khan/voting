package ru.vitalykhan.voting.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.vitalykhan.voting.To.MenuTo;
import ru.vitalykhan.voting.model.Menu;
import ru.vitalykhan.voting.model.Restaurant;
import ru.vitalykhan.voting.repository.MenuRepository;
import ru.vitalykhan.voting.repository.RestaurantRepository;
import ru.vitalykhan.voting.util.MenuUtil;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

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

    @GetMapping
    public Iterable<Menu> getAll() {
        log.info("Get all menus");
        return menuRepository.findAll();
    }

    @GetMapping("/{menuId}")
    public Menu getById(@PathVariable int menuId) {
        log.info("Get menu with id={}", menuId);
        return menuRepository.findById(menuId).orElse(null);
    }

    @GetMapping("/filter")
    public List<Menu> getByDate(@RequestParam
                                @NotNull
                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                        LocalDate date) {
        log.info("Get menus by date: {}", date);
        return menuRepository.findAllByDate(date);
    }

    @GetMapping("/todays")
    public List<Menu> getTodays() {
        log.info("Get today's menus");
        return menuRepository.findAllByDate(LocalDate.now());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAll() {
        log.info("Delete all menus");
        menuRepository.deleteAll();
    }

    @DeleteMapping("/{menuId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByID(@PathVariable int menuId) {
        log.info("Delete menu with id={}", menuId);
        menuRepository.deleteById(menuId);
    }

    @PostMapping(value = "/restaurants/{restaurantId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Menu> create(@RequestBody MenuTo menuTo, @PathVariable int restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
        Menu newMenu = menuRepository.save(MenuUtil.getMenuOf(menuTo, restaurant));
        log.info("Create menu {}", newMenu);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("menus/{id}")
                .buildAndExpand(newMenu.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(newMenu);
    }
}
