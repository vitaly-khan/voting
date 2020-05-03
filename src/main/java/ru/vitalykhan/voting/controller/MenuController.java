package ru.vitalykhan.voting.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.vitalykhan.voting.model.Menu;
import ru.vitalykhan.voting.repository.MenuRepository;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/menus", produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private MenuRepository repository;

    public MenuController(MenuRepository repository) {
        this.repository = repository;
    }


    @GetMapping
    public Iterable<Menu> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{menuId}")
    public Menu getMenu(@PathVariable int menuId) {
        return repository.findById(menuId).orElse(null);
    }

//    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Menu> create(@RequestBody Menu menu) {
//        Menu createdMenu = repository.save(menu);
//        TODO: complete method body
//        return null;
//    }

//    @DeleteMapping


    @GetMapping("/filter")
    public List<Menu> getByDate(@RequestParam
                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                        LocalDate date) {
        log.info("Get menus by date: {}", date);
        return repository.findByDateWithDishes(date);
    }
}
