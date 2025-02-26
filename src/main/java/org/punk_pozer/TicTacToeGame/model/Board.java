package org.punk_pozer.TicTacToeGame.model;

import jakarta.persistence.*;
import org.punk_pozer.TicTacToeGame.util.MatrixEnumConverter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "boards",
        indexes = {
        @Index(name = "idx_id", columnList = "id")
})
public class Board {

    @Id
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @Column(name = "is_player_first")
    boolean isUserFirst;

    @Column
    private LocalDateTime lastMoveTime;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Move> moves = new ArrayList<>();

    public Board() {
    }

    public Board(Long id, User user, GameStatus status, boolean isUserFirst, LocalDateTime lastMoveTime) {
        this.id = id;
        this.user = user;
        this.status = status;
        this.isUserFirst = isUserFirst;
        this.lastMoveTime = lastMoveTime;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public void setMoves(List<Move> moves) {
        this.moves = moves;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
