package ru.vitalykhan.voting.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.vitalykhan.voting.model.Dish;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface DishRepository extends CrudRepository<Dish, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Dish d WHERE d.id=:id")
    int delete(@Param("id") int id);

    //HSQLDB doesn't support syntax "SELECT EXISTS (SELECT ...)" returning boolean
    @Query(value = "SELECT COUNT(*) FROM DISH d WHERE d.MENU_ID=:id", nativeQuery = true)
    int countAllByMenuId(@Param("id") int id);

    @Query("SELECT d FROM Dish d JOIN FETCH d.menu WHERE d.id=:id")
    Dish findByIdWithMenu(@Param("id") int id);
}
