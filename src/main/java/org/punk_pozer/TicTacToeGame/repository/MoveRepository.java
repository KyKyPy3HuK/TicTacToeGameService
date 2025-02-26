package org.punk_pozer.TicTacToeGame.repository;

import org.punk_pozer.TicTacToeGame.model.Move;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoveRepository extends JpaRepository<Move, Long> {

}
