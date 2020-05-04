package ru.vitalykhan.voting.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.vitalykhan.voting.exception.IllegalRequestDataException;
import ru.vitalykhan.voting.exception.NotFoundException;
import ru.vitalykhan.voting.model.Restaurant;
import ru.vitalykhan.voting.repository.RestaurantRepository;

import java.net.URI;

@RestController
@RequestMapping(value = "/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private RestaurantRepository restaurantRepository;

    public RestaurantController(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping
    public Iterable<Restaurant> getAll() {
        log.info("Get all restaurants");
        return restaurantRepository.findAll();
    }

    @GetMapping("/{restaurantId}")
    public Restaurant getById(@PathVariable int restaurantId) {
        log.info("Get restaurants with id={}", restaurantId);
        return restaurantRepository.findById(restaurantId).orElse(null);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAll() {
        log.info("Delete all restaurants");
        restaurantRepository.deleteAll();
    }

    @DeleteMapping("/{restaurantId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteByID(@PathVariable int restaurantId) {
        log.info("Delete restaurant with id={}", restaurantId);
        restaurantRepository.deleteById(restaurantId);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> create(@RequestBody Restaurant restaurant) {
        Restaurant newRestaurant = restaurantRepository.save(restaurant);
        log.info("Create restaurant {}", newRestaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("restaurants/{id}")
                .buildAndExpand(newRestaurant.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(newRestaurant);
    }

    @PutMapping(value = "/{restaurantId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody Restaurant restaurant, @PathVariable int restaurantId) {
        //Reply conservatively, accept liberally
        if (restaurant.isNew()) {
            restaurant.setId(restaurantId);
        } else if (restaurant.getId() != restaurantId) {
            throw new IllegalRequestDataException("Restaurant must be with id=" + restaurantId);
        }

        if (restaurantRepository.findById(restaurantId).isPresent()) {
            log.info("Update restaurant {}", restaurant);
            restaurantRepository.save(restaurant);
        } else {
            throw new NotFoundException(restaurant + " doesn't exist in DB!");
        }
    }
}
