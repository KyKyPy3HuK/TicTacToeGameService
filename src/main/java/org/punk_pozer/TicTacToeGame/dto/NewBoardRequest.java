package org.punk_pozer.TicTacToeGame.dto;

/**
 * Класс нужен для Post запроса создания новой доски, хранит в себе информацию о том, кто на
 * новой доске будет ходить первым (играть за крестики)
 */
public class NewBoardRequest {
    private Boolean isPlayerFirst;

    // Геттеры и сеттеры
    public Boolean getIsPlayerFirst() {
        return isPlayerFirst;
    }

    public void setIsPlayerFirst(Boolean isPlayerFirst) {
        this.isPlayerFirst = isPlayerFirst;
    }
}