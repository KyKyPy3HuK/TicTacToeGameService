package org.punk_pozer.TicTacToeGame.util;

import org.punk_pozer.TicTacToeGame.model.Board;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.Random;

public class ComputerAi {

    private static final Random random = new Random(LocalDateTime.now().getSecond());

    /**
     * Размер стороны квадратной доски
     */
    private static final int SIZE = 3;

    /**
     * Env параметр, который определяет какая стратегия хода машины будет использована,
     * если true - случайная, иначе - оптимальная
     */
    @Value("${util.computer-ai.use-random-mode}")
    private static boolean randomMode;

    /**
     * Получить ход машины, если в параметрах random-mode = false, то ход будет оптимальным, иначе случайным
     * @param boardState текущее состояние доски
     * @param isPlayerFirst кто ходит первым, если true то игрок ходит первым за крестики
     * @return позицию хода pos = 0..8
     */
    public static int getMovePos(char[][] boardState, boolean isPlayerFirst){
        if (randomMode){
            return getRandMovePos( boardState, isPlayerFirst);
        }
        else {
            return getBestMovePos(boardState,isPlayerFirst);
        }
    }

    /**
     * Получить случайную доступную позицию хода на доске
     * @param boardState текущее состояние доски
     * @param isPlayerFirst определяет, кто ходит первым - игрок/машина
     * @return случайное доступное на доске значение от 0 до 8
     */
    public static int getRandMovePos(char[][] boardState, boolean isPlayerFirst){
        int x;
        int y;
        do {
            x = random.nextInt(0, 3);
            y = random.nextInt(0, 3);
        } while (boardState[y][x] != Board.FREE);

        return y * 3 + x;
    }

    /**
     * Минимакс с альфа-бета отсечением, нужна для расчета оптимального хода машины
     * @param boardState текущее состояние доски
     * @param depth глубина
     * @param isMaximizing
     * @param alpha коэффициент
     * @param beta коэффициент
     * @param playerSymbol символ игрока
     * @param computerSymbol символ компьютера
     * @return оптимальную позицию хода на доске
     */
    private static int minimax(char[][] boardState, int depth, boolean isMaximizing, int alpha, int beta, char playerSymbol, char computerSymbol) {
        // Проверка на победу компьютера
        if (isWin(boardState, computerSymbol)) {
            return 10 - depth;
        }
        // Проверка на победу игрока
        if (isWin(boardState, playerSymbol)) {
            return depth - 10;
        }
        // Проверка на ничью
        if (isDraw(boardState)) {
            return 0;
        }

        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;
            for (int y = 0; y < SIZE; y++) {
                for (int x = 0; x < SIZE; x++) {
                    if (boardState[y][x] == Board.FREE) {
                        boardState[y][x] = computerSymbol;
                        int eval = minimax(boardState, depth + 1, false, alpha, beta, playerSymbol, computerSymbol);
                        boardState[y][x] = Board.FREE;
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
                    if (boardState[y][x] == Board.FREE) {
                        boardState[y][x] = playerSymbol;
                        int eval = minimax(boardState, depth + 1, true, alpha, beta, playerSymbol, computerSymbol);
                        boardState[y][x] = Board.FREE;
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

    /**
     * Проверка на победу, нужна для поиска оптимального хода
     * @param boardState текущее состояние доски
     * @param symbol символ, который будет проверяться на победу
     * @return true если субъект использующий symbol одержал победу, иначе false
     */
    private static boolean isWin(char[][] boardState, char symbol) {
        // Проверка строк и столбцов
        for (int i = 0; i < SIZE; i++) {
            if ((boardState[i][0] == symbol && boardState[i][1] == symbol && boardState[i][2] == symbol) ||
                    (boardState[0][i] == symbol && boardState[1][i] == symbol && boardState[2][i] == symbol)) {
                return true;
            }
        }
        // Проверка диагоналей
        if ((boardState[0][0] == symbol && boardState[1][1] == symbol && boardState[2][2] == symbol) ||
                (boardState[0][2] == symbol && boardState[1][1] == symbol && boardState[2][0] == symbol)) {
            return true;
        }
        return false;
    }

    // Проверка на ничью
    private static boolean isDraw(char[][] boardState) {
        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                if (boardState[y][x] == Board.FREE) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Получение оптимального хода машины на доске
     * @param boardState текущее состояние доски
     * @param isPlayerFirst определяет, кто ходит первым в партии, а также определяет символы субъектов игры (машины/игрока)
     * @return оптимальный ход машины
     */
    public static int getBestMovePos(char[][] boardState, boolean isPlayerFirst) {
        char playerSymbol = isPlayerFirst ? Board.CROSS : Board.ZERO;
        char computerSymbol = isPlayerFirst ? Board.ZERO : Board.CROSS;

        int bestVal = Integer.MIN_VALUE;
        int[] bestMove = new int[]{-1, -1};

        for (int y = 0; y < SIZE; y++) {
            for (int x = 0; x < SIZE; x++) {
                if (boardState[y][x] == Board.FREE) {
                    boardState[y][x] = computerSymbol;
                    int moveVal = minimax(boardState, 0, false, Integer.MIN_VALUE, Integer.MAX_VALUE, playerSymbol, computerSymbol);
                    boardState[y][x] = Board.FREE;

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
