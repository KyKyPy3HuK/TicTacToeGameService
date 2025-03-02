package org.punk_pozer.TicTacToeGame.controller;

import jakarta.servlet.http.HttpSession;
import org.punk_pozer.TicTacToeGame.dto.BoardDTO;
import org.punk_pozer.TicTacToeGame.exception.BoardNotFoundException;
import org.punk_pozer.TicTacToeGame.exception.IllegalMoveUndoException;
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
    /**
     * Имя параметра сессии в котором хранится идентификатор доски
     */
    private static final String BOARD_ID_SESS_ATR = "boardId";

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService){
        this.gameService = gameService;
    }

    /**
     * Создает и возвращает новую доску, если была старая то присваивает ей статус досрочного завершения "FINISHED"
     * @return новаая доска и статус 201 CREATED
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
     * @param session сессия пользователя
     * @return доска пользователя, если существует, иначе выбрасывает BoardNotFoundException
     * @exception BoardNotFoundException 404:NOT FOUND - пользователь не имеет доски
     */
    @GetMapping("")
    public ResponseEntity<?> getBoard(HttpSession session) throws BoardNotFoundException{
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
     * Сделать ход на доске
     * @param pos позиция для хода, принимает значения от 0 до 8,
     *           раcчитывается по формуле координат на доске: y * 3 + x
     * @param session сессия пользователя
     * @return доска с ходом игрока и ответным ходом машины и статус 201:CREATED
     * @throws IllegalMoveException 403:FORBIDDEN - pos хода некорректный
     * @throws BoardNotFoundException 404:NOT FOUND - пользователь не имеет доски
     */
    @GetMapping("/move")
    public ResponseEntity<?> makeMove(
            @RequestParam(name = "pos", required = true) int pos,
            HttpSession session) throws IllegalMoveException, BoardNotFoundException{
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

    /**
     * Выполнить отмену последнего хода игрока и последнего хода машины, или только хода машины если игрок еще не ходил
     * @param session сессия пользователя
     * @return доску с отмененными ходом/ходами
     * @throws IllegalMoveUndoException 403:FORBIDDEN - в данном состоянии доски ход/ходы нельзя отменить
     * @throws BoardNotFoundException 404:NOT FOUND - пользователь не имеет доски
     */
    @GetMapping("/undo")
    public ResponseEntity<?> redoMove(HttpSession session) throws IllegalMoveUndoException, BoardNotFoundException {
        //Проверка на наличие данных о доске в сессии
        Long boardId = (Long)session.getAttribute(BOARD_ID_SESS_ATR);
        if (boardId == null){
            throw new BoardNotFoundException();
        }

        //Проверка на наличие доски по данным сессии
        Optional<Board> boardOptional = gameService.getBoardById(boardId);
        if (boardOptional.isEmpty()){
            throw new BoardNotFoundException();
        }

        BoardDTO boardDTO = new BoardDTO(gameService.undoMove(boardOptional.get()));

        return new ResponseEntity<BoardDTO>(boardDTO, HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<String> illegalMoveExceptionHandler(IllegalMoveException e){
        return new ResponseEntity<String>("Error 403 - Forbidden: " + e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    private ResponseEntity<String> boardNotFoundExceptionHandler(BoardNotFoundException e){
        return new ResponseEntity<String>("Error 404 - Not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<String> illegalMoveUndoExceptionHandler(IllegalMoveUndoException e){
        return new ResponseEntity<String>("Error 403 - Forbidden: " + e.getMessage(), HttpStatus.FORBIDDEN);
    }

}
