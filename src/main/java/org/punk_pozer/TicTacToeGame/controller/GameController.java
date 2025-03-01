package org.punk_pozer.TicTacToeGame.controller;

import jakarta.servlet.http.HttpSession;
import org.punk_pozer.TicTacToeGame.dto.BoardDTO;
import org.punk_pozer.TicTacToeGame.exception.BoardNotFoundException;
import org.punk_pozer.TicTacToeGame.model.Board;
import org.punk_pozer.TicTacToeGame.service.GameService;
import org.punk_pozer.TicTacToeGame.exception.IllegalMoveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private static final String BOARD_ID_SESS_ATR = "boardId";

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService){
        this.gameService = gameService;
        System.out.println("GameController start");
    }

    /**
     *
     * @return OK
     */
    @GetMapping("/new")
    public ResponseEntity<?> newBoard(
            @RequestParam(name = "first", required = false ,defaultValue = "true") boolean isPlayerFirst, HttpSession session){

        Board newBoard;
        Long oldBoardId = (Long)session.getAttribute(BOARD_ID_SESS_ATR);

        //Смена статуса предыдущей доски на "ENDED" если она существует
        if (oldBoardId != null){
            Optional<Board> oldBoard = gameService.getBoardById(oldBoardId);
            if (oldBoard.isPresent()){
                gameService.finalizeBoard(oldBoard.get());
            }
        }
        //Создание новой доски
        newBoard = gameService.getNewBoard(isPlayerFirst);
        Long newBoardId = newBoard.getId();
        session.setAttribute(BOARD_ID_SESS_ATR, newBoardId);
        BoardDTO dto = new BoardDTO(newBoard);
        return new ResponseEntity<BoardDTO>(dto, HttpStatus.CREATED);
    }

    /**
     * Возврат текущей доски пользователя
     * @return
     */
    @GetMapping("")
    public ResponseEntity<?> getBoard(HttpSession session){
        Long boardId = (Long)session.getAttribute(BOARD_ID_SESS_ATR);

        //Проверка наличия идентификатора доски в сессии
        if (boardId != null){
            Optional<Board> board = gameService.getBoardById(boardId);
            //Проверка наличия доски по данному идентификатору
            if (board.isPresent()){
                //Успех
                BoardDTO dto = new BoardDTO(board.get());
                return new ResponseEntity<BoardDTO>(dto, HttpStatus.OK);
            }
        }
        //Ошибка
        throw new BoardNotFoundException();
    }

    /**
     *
     * @return
     */
    @GetMapping("/move")
    public ResponseEntity<?> makeMove(
            @RequestParam(name = "pos", required = true) int pos,
            HttpSession session){
        //Проверка на наличие данных в сессии
        Long boardId = (Long)session.getAttribute(BOARD_ID_SESS_ATR);
        if (boardId == null){
            throw new BoardNotFoundException();
        }

        //Проверка на наличие доски по данным сессии
        Optional<Board> boardOptional = gameService.getBoardById(boardId);
        if (boardOptional.isEmpty()){
            throw new BoardNotFoundException();
        }

        //Ход
        BoardDTO boardDto = new BoardDTO(gameService.makeMove(boardOptional.get(), pos));

        return new ResponseEntity<BoardDTO>(boardDto,HttpStatus.CREATED);
    }

    //Отменить ход
    @GetMapping("/undo")
    public ResponseEntity<?> redoMove(){
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<String> handleIllegalMoveException(IllegalMoveException e){
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    private ResponseEntity<String> handleBoardNotFoundException(BoardNotFoundException e){
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

}
