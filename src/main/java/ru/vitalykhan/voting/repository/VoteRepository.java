package ru.vitalykhan.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.vitalykhan.voting.model.Vote;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {

    Vote findByDateAndUserId(LocalDate date, int userId);

    @Query("SELECT v FROM Vote v JOIN FETCH v.menu m JOIN FETCH m.restaurant WHERE v.date=:date ORDER BY v.id")
    List<Vote> findAllByDateWithRestaurants(@Param("date") LocalDate date);

    @Query("SELECT v FROM Vote v JOIN FETCH v.menu m JOIN FETCH m.restaurant WHERE v.user.id=:id ORDER BY v.id")
    List<Vote> findAllByUserIdWithRestaurants(@Param("id") int userId);
}
