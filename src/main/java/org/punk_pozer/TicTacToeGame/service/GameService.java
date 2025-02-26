package org.punk_pozer.TicTacToeGame.service;


import org.punk_pozer.TicTacToeGame.model.Board;
import org.punk_pozer.TicTacToeGame.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class GameService {


    private final GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository){
        this.gameRepository = gameRepository;
        System.out.println("GameService start");
    }

    public Board getNewBoard(Long user_id, boolean isPlayerFirst){
        Board newBoard = new Board();
        newBoard.setPlayerFirst(isPlayerFirst);
        newBoard.setUserId(user_id);
        newBoard.setLastMoveTime(LocalDateTime.now());
        return new Board();
    }

    public HttpStatus tryMakeMove(int boardId, int position){
        return HttpStatus.NOT_IMPLEMENTED;
    }

    public HttpStatus tryRedoMove(int boardId){
        return HttpStatus.NOT_IMPLEMENTED;
    }

}
