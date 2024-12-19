package xyz.hw2.myledger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.hw2.myledger.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
