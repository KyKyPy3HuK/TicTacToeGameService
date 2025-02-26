package org.punk_pozer.TicTacToeGame.util;

public class IllegalMoveException extends RuntimeException {
    public IllegalMoveException(String message) {
        super(message);
    }
}
