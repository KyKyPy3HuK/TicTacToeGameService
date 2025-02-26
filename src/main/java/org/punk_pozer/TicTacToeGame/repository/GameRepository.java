package org.punk_pozer.TicTacToeGame.repository;

import jakarta.transaction.Transactional;
import org.punk_pozer.TicTacToeGame.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface  GameRepository extends JpaRepository<Board, Long> {
    @Transactional
    Optional<Board> getBoardById(Long id);
}
