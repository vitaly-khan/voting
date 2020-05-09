package ru.vitalykhan.voting.repository;

import org.springframework.data.repository.CrudRepository;
import ru.vitalykhan.voting.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmail(String email);
}
