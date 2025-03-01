package org.punk_pozer.TicTacToeGame.util;

import org.punk_pozer.TicTacToeGame.model.Board;

import java.time.LocalDateTime;
import java.util.Random;

public class ComputerAi {

    private static final Random random = new Random(LocalDateTime.now().getSecond());

    public static int getRandMovePos(char[][] boardState, boolean isPlayerFirst){
        int x;
        int y;
        do {
            x = random.nextInt(0, 3);
            y = random.nextInt(0, 3);
        } while (boardState[y][x] != Board.FREE);

        return y * 3 + x;
    }


}
