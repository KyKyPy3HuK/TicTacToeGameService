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

    public static final char ZERO = 'O';
    public static final char FREE = ' ';
    public static final char CROSS = 'X';

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private GameStatus status;

    @Column(name = "is_player_first")
    boolean isUserFirst;

    @Column(name = "lastMoveTime",columnDefinition = "timestamp(7)")
    private LocalDateTime lastMoveTime;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Move> moves = new ArrayList<>();

    public Board() {
    }

    public Board(Long id, GameStatus status, boolean isUserFirst, LocalDateTime lastMoveTime) {
        this.id = id;
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

    public char[][] getState(){
        char[][] boardMatrix = new char[][] {{FREE,FREE,FREE},{FREE,FREE,FREE},{FREE,FREE,FREE}};

        char playerMark;
        char computerMark;
        if (this.isUserFirst()){
            playerMark = CROSS;
            computerMark = ZERO;
        }
        else{
            playerMark = ZERO;
            computerMark = CROSS;
        };
        // Выставление ходов на доску
        int x = 0;
        int y = 0;
        int pos = 0;
        for (Move move : this.moves){
            //Расчет позиции
            pos = move.getPosition();
            x = pos % 3;
            y = pos / 3;

            if (move.isPlayerMove()){
                boardMatrix[y][x] = playerMark;
            }
            else{
                boardMatrix[y][x] = computerMark;
            }
        }

        return boardMatrix;
    }
}
