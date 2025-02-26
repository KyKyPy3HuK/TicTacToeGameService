package org.punk_pozer.TicTacToeGame.repository;

import jakarta.transaction.Transactional;
import jdk.jfr.Registered;
import org.punk_pozer.TicTacToeGame.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Transactional
    Optional<User> getUserById(Long id);
}
