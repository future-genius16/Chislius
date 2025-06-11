package ru.hse.chislius_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hse.chislius_server.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByToken(String token);
    boolean existsByUsername(String username);
    boolean existsByToken(String token);
}