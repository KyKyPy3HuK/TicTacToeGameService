package org.punk_pozer.TicTacToeGame.service;


import jakarta.transaction.Transactional;
import org.punk_pozer.TicTacToeGame.model.Board;
import org.punk_pozer.TicTacToeGame.model.GameStatus;
import org.punk_pozer.TicTacToeGame.model.User;
import org.punk_pozer.TicTacToeGame.repository.BoardRepository;
import org.punk_pozer.TicTacToeGame.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {


    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @Autowired
    public GameService(BoardRepository boardRepository, UserRepository userRepository){
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        System.out.println("GameService start");
    }

    /**
     *  Получить акутальную доску пользователя (на которой был сделан последний ход пользователя)
     * @param userId идентификатор пользователя
     * @return actualBoard or null
     * */
    public Optional<Board> getActualBoardByUser(Long userId) {
        List<Board> boards = boardRepository.getBoardsByUserId(userId);
        return boards.stream().max(Comparator.comparing(Board::getLastMoveTime));
    }

    @Transactional
    public Board addNewBoard(Long userId, boolean isPlayerFirst){

        User user = userRepository.getUserById(userId).orElse(null);

        Board newBoard = new Board();

        newBoard.setUserFirst(isPlayerFirst);
        newBoard.setUser(user);
        newBoard.setLastMoveTime(LocalDateTime.now());
        newBoard.setStatus(GameStatus.STARTED);
        boardRepository.save(newBoard);
        return newBoard;
    }

    @Transactional
    public User addNewUser(){
        User newUser = new User();
        userRepository.save(newUser);
        return newUser;
    }

    public HttpStatus tryMakeMove(int boardId, int position){
        return HttpStatus.NOT_IMPLEMENTED;
    }

    public HttpStatus tryRedoMove(int boardId){
        return HttpStatus.NOT_IMPLEMENTED;
    }

}
