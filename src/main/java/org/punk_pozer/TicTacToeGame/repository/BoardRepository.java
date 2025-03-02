package org.punk_pozer.TicTacToeGame.repository;

import jakarta.transaction.Transactional;
import org.punk_pozer.TicTacToeGame.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    @Transactional
    Optional<Board> getBoardById(Long id);


    /**
     * Удаляет все записи сделанные раньше чем cutoffTime
     * @param cutoffTime время по которому будут удалены старые записи
     */
    @Transactional
    @Modifying
    @Query("delete from Board b where b.lastMoveTime < :cutoffTime")
    int deleteOldBoards(@Param("cutoffTime") LocalDateTime cutoffTime);
}
