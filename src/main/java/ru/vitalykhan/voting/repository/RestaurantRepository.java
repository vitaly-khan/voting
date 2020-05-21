package ru.vitalykhan.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.vitalykhan.voting.model.Restaurant;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    @Modifying
    @Transactional
    @Query("DELETE FROM Restaurant r WHERE r.id=:id")
    int delete(@Param("id") int id);


    List<Restaurant> findByEnabledTrueOrderByName();

    Restaurant findByEnabledTrueAndId(int id);

    //Cascade disabling
    //Can be optimized on db level
    @Modifying
    @Transactional
    @Query(value = "UPDATE MENU SET ENABLED=FALSE WHERE ID IN " +
            "(SELECT m.ID FROM RESTAURANT r JOIN MENU m ON r.ID=m.RESTAURANT_ID WHERE r.ID=:id)",
            nativeQuery = true)
    void cascadeMenuDisabling(@Param("id") int id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE DISH SET ENABLED=FALSE WHERE ID IN " +
            "(SELECT d.ID FROM RESTAURANT r JOIN MENU m ON r.ID=m.RESTAURANT_ID " +
            "JOIN DISH d on m.ID=d.MENU_ID WHERE r.ID=:id);",
            nativeQuery = true)
    void cascadeDishDisabling(@Param("id") int id);
}
