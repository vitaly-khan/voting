package ru.vitalykhan.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.vitalykhan.voting.model.Vote;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {

    Vote findByDateAndUserId(LocalDate date, int userId);

    List<Vote> findAllByDate(LocalDate date);

    List<Vote> findAllByUserId(int userId);
}
