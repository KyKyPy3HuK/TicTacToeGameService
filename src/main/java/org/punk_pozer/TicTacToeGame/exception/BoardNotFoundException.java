package org.punk_pozer.TicTacToeGame.exception;

public class BoardNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Board not found, create new board using - /api/game/new";

    public BoardNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public BoardNotFoundException(String message) {
        super(message);
    }
}
