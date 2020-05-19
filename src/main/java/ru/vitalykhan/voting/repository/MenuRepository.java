package ru.vitalykhan.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.vitalykhan.voting.model.Menu;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface MenuRepository extends JpaRepository<Menu, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Menu m WHERE m.id=:id")
    int delete(@Param("id") int id);

    @Query("SELECT DISTINCT m FROM Menu m JOIN FETCH m.restaurant JOIN FETCH m.dishes WHERE m.date=:date ORDER BY m.restaurant.name")
    List<Menu> findNotEmptyByDateOrderedByRestaurantName(@Param("date") LocalDate date);

    @Query("SELECT DISTINCT m FROM Menu m JOIN FETCH m.restaurant LEFT JOIN FETCH m.dishes WHERE m.date=:date ORDER BY m.restaurant.name")
    List<Menu> findAllByDateOrderedByRestaurantName(@Param("date") LocalDate date);

    @Query("SELECT m FROM Menu m JOIN FETCH m.restaurant LEFT JOIN FETCH m.dishes WHERE m.id=:id")
    Menu findByIdWithRestaurantAndDishes(@Param("id") int id);

    //HSQLDB doesn't support syntax "SELECT EXISTS (SELECT ...)" returning boolean
    @Query(value = "SELECT COUNT(*) FROM MENU m WHERE m.RESTAURANT_ID=:id", nativeQuery = true)
    int countAllByIdRestaurant(@Param("id") int id);

}
