package org.punk_pozer.TicTacToeGame.util;

import org.punk_pozer.TicTacToeGame.model.Board;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.Random;

public class ComputerAi {

    private static final Random random = new Random(LocalDateTime.now().getSecond());

    private static final int SIZE = 3;

    private static final char EMPTY = Board.FREE;

    @Value("${util.computer-ai.use-random-mode}")
    private static boolean randomMode;

    public static int getMovePos(char[][] boardState, boolean isPlayerFirst){
        if (randomMode){
            return getRandMovePos( boardState, isPlayerFirst);
        }
        else {
            return findBestMove(boardState,isPlayerFirst);
        }
    }

    public static int getRandMovePos(char[][] boardState, boolean isPlayerFirst){
        int x;
        int y;
        do {
            x = random.nextInt(0, 3);
            y = random.nextInt(0, 3);
        } while (boardState[y][x] != Board.FREE);

        return y * 3 + x;
    }


    // Минимакс с альфа-бета отсечением
    private static int minimax(char[][] board, int depth, boolean isMaximizing, int alpha, int beta, char playerSymbol, char computerSymbol) {
        // Проверка на победу компьютера
        if (isWin(board, computerSymbol)) {
            return 10 - depth;
        }
        // Проверка на победу игрока
        if (isWin(board, playerSymbol)) {
            return depth - 10;
        }
        // Проверка на ничью
        if (isDraw(board)) {
            return 0;
        }

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (int y = 0; y < SIZE; y++) {
                for (int x = 0; x < SIZE; x++) {
                    if (board[y][x] == EMPTY) {
                        board[y][x] = computerSymbol;
                        int eval = minimax(board, depth + 1, false, alpha, beta, playerSymbol, computerSymbol);
                        board[y][x] = EMPTY;
                        maxEval = Math.max(maxEval, eval);
                        alpha = Math.max(alpha, eval);
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int y = 0; y < SIZE; y++) {
                for (int x = 0; x < SIZE; x++) {
                    if (board[y][x] == EMPTY) {
                        board[y][x] = playerSymbol;
                        int eval = minimax(board, depth + 1, true, alpha, beta, playerSymbol, computerSymbol);
                        board[y][x] = EMPTY;
                        minEval = Math.min(minEval, eval);
                        beta = Math.min(beta, eval);
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
            }
            return minEval;
        }
    }

    // Проверка на победу
    private static boolean isWin(char[][] board, char symbol) {
        // Проверка строк и столбцов
        for (int i = 0; i < SIZE; i++) {
            if ((board[i][0] == symbol && board[i][1] == symbol && board[i][2] == symbol) ||
                    (board[0][i] == symbol && board[1][i] == symbol && board[2][i] == symbol)) {
                return true;
            }
        }
        // Проверка диагоналей
        if ((board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) ||
                (board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol)) {
            return true;
        }
        return false;
    }

    // Проверка на ничью
    private static boolean isDraw(char[][] board) {
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                if (board[y][x] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    // Нахождение лучшего хода для компьютера
    public static int findBestMove(char[][] board, boolean isPlayerFirst) {
        char playerSymbol = isPlayerFirst ? Board.CROSS : Board.ZERO;
        char computerSymbol = isPlayerFirst ? Board.ZERO : Board.CROSS;

        int bestVal = Integer.MIN_VALUE;
        int[] bestMove = new int[]{-1, -1};

        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                if (board[y][x] == EMPTY) {
                    board[y][x] = computerSymbol;
                    int moveVal = minimax(board, 0, false, Integer.MIN_VALUE, Integer.MAX_VALUE, playerSymbol, computerSymbol);
                    board[y][x] = EMPTY;

                    if (moveVal > bestVal) {
                        bestMove[0] = y;
                        bestMove[1] = x;
                        bestVal = moveVal;
                    }
                }
            }
        }
        return bestMove[0] * 3 + bestMove[1];
    }


}
