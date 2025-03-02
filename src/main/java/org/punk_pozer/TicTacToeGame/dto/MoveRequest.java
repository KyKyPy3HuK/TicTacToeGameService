package org.punk_pozer.TicTacToeGame.dto;

/**
 * Класс нужен для Post запроса создания хода, хранит в позицию хода pos
 */
public class MoveRequest {
    private int pos;

    // Геттеры и сеттеры
    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}