package ru.vitalykhan.voting.repository;

import org.springframework.data.repository.CrudRepository;
import ru.vitalykhan.voting.model.Restaurant;

public interface RestaurantRepository extends CrudRepository<Restaurant, Integer> {
}
