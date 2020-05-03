package ru.vitalykhan.voting.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.vitalykhan.voting.model.Menu;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface MenuRepository extends CrudRepository<Menu, Integer> {

    @Query("SELECT m FROM Menu m WHERE m.date=:date")
//    @Query("SELECT m FROM Menu m JOIN FETCH Dish d WHERE m.date=:date")
    List<Menu> findByDateWithDishes(@Param("date") LocalDate date);

}
