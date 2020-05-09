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
import ru.vitalykhan.voting.util.ValidationUtil;
import ru.vitalykhan.voting.util.exception.NotFoundException;

import java.net.URI;
import java.util.List;

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
        return dishRepository.findById(dishId).orElse(null);
    }

    @DeleteMapping("/{dishId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByID(@PathVariable int dishId) {
        log.info("Delete menu with id={}", dishId);
        dishRepository.deleteById(dishId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<Dish> create(@RequestBody DishTo dishTo) {
        ValidationUtil.checkIsNew(dishTo);

        Integer menuId = dishTo.getMenuId();
        Menu menu = menuRepository.findById(menuId).orElse(null);
        if (menu == null) {
            throw new NotFoundException(String.format("Menu with id=%s wasn't found!", menuId));
        }
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
    public void update(@RequestBody DishTo dishTo, @PathVariable int id) {
        ValidationUtil.assureIdConsistency(dishTo, id);

        if (dishRepository.findById(id).isEmpty()) {
            throw new NotFoundException(String.format(
                    "Dish with id=%d doesn't exist in DB!", dishTo.getId()));
        } else {
            log.info("Update dish with id={}", id);
            Menu menu = menuRepository.findById(dishTo.getMenuId()).orElse(null);
            dishRepository.save(DishUtil.of(dishTo, menu));
        }
    }
}