package org.punk_pozer.TicTacToeGame.dto;

import org.punk_pozer.TicTacToeGame.model.Board;
import org.punk_pozer.TicTacToeGame.model.GameStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Объект предоставляемый пользователю, который содержит нужную пользователю информацию,
 * а также агрегированное по ходам текущее состояние доски
 */
@Component
public class BoardDTO {

    private Long id;
    private char[][] state;
    private GameStatus status;
    private boolean isUserFirst;
    private LocalDateTime lastMoveTime;

    public BoardDTO(){}

    public BoardDTO(final Board board){
        this.id = board.getId();
        this.state = board.getState();
        this.status = board.getStatus();
        this.isUserFirst = board.isUserFirst();
        this.lastMoveTime = board.getLastMoveTime();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public char[][] getState() {
        return state;
    }

    public void setState(char[][] state) {
        this.state = state;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public boolean isUserFirst() {
        return isUserFirst;
    }

    public void setUserFirst(boolean userFirst) {
        isUserFirst = userFirst;
    }

    public LocalDateTime getLastMoveTime() {
        return lastMoveTime;
    }

    public void setLastMoveTime(LocalDateTime lastMoveTime) {
        this.lastMoveTime = lastMoveTime;
    }
}
