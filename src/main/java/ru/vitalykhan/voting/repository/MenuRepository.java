package ru.vitalykhan.voting.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
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
public interface MenuRepository extends JpaRepository<Menu, Integer> {

    @Query("SELECT DISTINCT m FROM Menu m JOIN FETCH m.restaurant WHERE m.date=:date")
    List<Menu> findAllByDate(@Param("date") LocalDate date);

    //    Left this chunk of code in case I'll need to change to LAZY for menu.restaurant

//    @Query("SELECT DISTINCT m FROM Menu m JOIN FETCH m.restaurant")
//    List<Menu> findAll();
//    @Query("SELECT DISTINCT m FROM Menu m JOIN FETCH m.restaurant")
//    List<Menu> findByID();


}
