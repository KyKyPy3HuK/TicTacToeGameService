package org.punk_pozer.TicTacToeGame.repository;

import jakarta.transaction.Transactional;
import org.punk_pozer.TicTacToeGame.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    @Transactional
    Optional<Board> getBoardById(Long id);
}
