package ru.vitalykhan.voting.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.vitalykhan.voting.model.Dish;
import ru.vitalykhan.voting.model.Menu;
import ru.vitalykhan.voting.repository.DishRepository;
import ru.vitalykhan.voting.repository.MenuRepository;
import ru.vitalykhan.voting.to.DishTo;
import ru.vitalykhan.voting.util.DishUtil;

import javax.validation.Valid;
import java.net.URI;

import static ru.vitalykhan.voting.util.ValidationUtil.*;

@RestController
@RequestMapping(value = "/dishes", produces = MediaType.APPLICATION_JSON_VALUE)
public class DishController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private MenuRepository menuRepository;
    private DishRepository dishRepository;

    public DishController(MenuRepository menuRepository, DishRepository dishRepository) {
        this.menuRepository = menuRepository;
        this.dishRepository = dishRepository;
    }

    @GetMapping("/{dishId}")
    public Dish getById(@PathVariable int dishId) {
        log.info("Get dish with id={}", dishId);
        Dish dish = dishRepository.findById(dishId).orElse(null);
        checkFound(dish != null, dishId, getClass());
        return dish;
    }

    @DeleteMapping("/{dishId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByID(@PathVariable int dishId) {
        log.info("Delete menu with id={}", dishId);
        checkFound(dishRepository.delete(dishId) != 0, dishId, getClass());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<Dish> create(@Valid @RequestBody DishTo dishTo) {
        checkIsNew(dishTo);

        int menuId = dishTo.getMenuId();
        Menu menu = menuRepository.getOne(menuId);
        checkFound(menu != null, menuId, Menu.class);

        Dish newDish = dishRepository.save(DishUtil.of(dishTo, menu));
        log.info("Create a new dish {}", newDish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("dishes/{id}")
                .buildAndExpand(newDish.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(newDish);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Transactional
    public void update(@Valid @RequestBody DishTo dishTo, @PathVariable int id) {
        log.info("Update dish with id={}", id);
        assureIdConsistency(dishTo, id);
        checkFound(dishRepository.findById(id).isPresent(), id, getClass());

        int menuId = dishTo.getMenuId();
        Menu menu = menuRepository.getOne(menuId);
        checkFound(menu != null, menuId, Menu.class);

        dishRepository.save(DishUtil.of(dishTo, menu));
    }
}
