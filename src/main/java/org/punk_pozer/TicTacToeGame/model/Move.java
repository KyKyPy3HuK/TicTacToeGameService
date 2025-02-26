package org.punk_pozer.TicTacToeGame.model;


import jakarta.persistence.*;

@Entity
public class Move {

    @Id
    @Column(name = "id")
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(name = "number")
    private int number;

    @Column(name = "is_player_move")
    private boolean isPlayerMove;

    @Column(name = "position")
    private int position;

    public Move(){}


    public Move(int position, boolean isPlayerMove, int number, Board board, Long id) {
        this.position = position;
        this.isPlayerMove = isPlayerMove;
        this.number = number;
        this.board = board;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isPlayerMove() {
        return isPlayerMove;
    }

    public void setPlayerMove(boolean playerMove) {
        isPlayerMove = playerMove;
    }

    public void setPosition(int position){
        this.position = position;
    }

    public int getPosition(){
        return position;
    }
}
