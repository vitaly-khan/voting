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

    // Return all not empty, enabled, today's menus
    // with restaurants and enabled dishes
    // ordered by restaurant name
    @Query("SELECT DISTINCT m FROM Menu m JOIN FETCH m.restaurant r JOIN FETCH m.dishes d WHERE m.date=:date AND d.enabled=true ORDER BY r.name")
    List<Menu> getTodays(@Param("date") LocalDate date);

    @Query("SELECT DISTINCT m FROM Menu m JOIN FETCH m.restaurant LEFT JOIN FETCH m.dishes WHERE m.date=:date")
    List<Menu> findAllByDate(@Param("date") LocalDate date);

    @Query("SELECT DISTINCT m FROM Menu m JOIN FETCH m.restaurant LEFT JOIN FETCH m.dishes d WHERE m.date=:date AND m.enabled=true AND d.enabled=true")
    List<Menu> findAllEnabledByDate(@Param("date") LocalDate date);

    @Query("SELECT m FROM Menu m JOIN FETCH m.restaurant LEFT JOIN FETCH m.dishes WHERE m.id=:id")
    Menu findByIdWithRestaurantAndDishes(@Param("id") int id);

    //HSQLDB doesn't support syntax "SELECT EXISTS (SELECT ...)" returning boolean
    @Query(value = "SELECT COUNT(m) FROM Menu m WHERE m.restaurant.id=:id")
    int countAllByIdRestaurant(@Param("id") int id);

    Menu findByEnabledTrueAndId(int id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE DISH SET ENABLED=FALSE WHERE ID IN " +
            "(SELECT d.ID FROM MENU m JOIN DISH d on m.ID=d.MENU_ID WHERE m.ID=:id);",
            nativeQuery = true)
    void cascadeDishDisabling(@Param("id") int menuId);
}
