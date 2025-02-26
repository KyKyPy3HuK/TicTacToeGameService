package org.punk_pozer.TicTacToeGame.model;

import jakarta.persistence.*;
import org.punk_pozer.TicTacToeGame.util.MatrixEnumConverter;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "boards", indexes = {
        @Index(name = "idx_id", columnList = "id")
})
public class Board {

    @Id
    @Column(name = "id")
    private Long id;


    @Column(name = "user_id")
    private Long userId;

    @Column(name = "state")
    @Convert(converter = MatrixEnumConverter.class)
    private CellState[][] state;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @Column(name = "is_player_first")
    boolean isPlayerFirst;

    @Column
    private LocalDateTime lastMoveTime;

    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Move> moves;

    public Board() {
    }

    public Board(Long id, Long userId,CellState[][] state, GameStatus status, boolean isPlayerFirst, LocalDateTime lastMoveTime) {
        this.id = id;
        this.userId = userId;
        this.state = state;
        this.status = status;
        this.isPlayerFirst = isPlayerFirst;
        this.lastMoveTime = lastMoveTime;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public void setMoves(List<Move> moves) {
        this.moves = moves;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CellState[][] getState() {
        return state;
    }

    public void setState(CellState[][] state) {
        this.state = state;
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public boolean isPlayerFirst() {
        return isPlayerFirst;
    }

    public void setPlayerFirst(boolean playerFirst) {
        isPlayerFirst = playerFirst;
    }

    public LocalDateTime getLastMoveTime() {
        return lastMoveTime;
    }

    public void setLastMoveTime(LocalDateTime lastMoveTime) {
        this.lastMoveTime = lastMoveTime;
    }
}
