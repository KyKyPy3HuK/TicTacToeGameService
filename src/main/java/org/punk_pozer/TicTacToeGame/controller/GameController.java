package org.punk_pozer.TicTacToeGame.controller;

import jakarta.servlet.http.HttpSession;
import org.punk_pozer.TicTacToeGame.model.User;
import org.punk_pozer.TicTacToeGame.service.GameService;
import org.punk_pozer.TicTacToeGame.util.IllegalMoveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private static int board_counter = 0;

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
    public ResponseEntity<?> startNewGame(){

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Возврат текущей доски пользователя
     * @return
     */
    @GetMapping("")
    public ResponseEntity<?> getActualGameByUser(HttpSession session){
        //Проверить, существует ли пользователь в системе
        Long userId = (Long)session.getAttribute("userId");
        if (userId != null){

        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     *
     * @return
     */
    @GetMapping("/move")
    public ResponseEntity<?> moveInGameById(HttpSession session){
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //Отменить ход
    @GetMapping("/redo")
    public ResponseEntity<?> redoInGameById(){
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/check")
    public ResponseEntity<String> checkSession(HttpSession session){

        String message = mapUserIdBySession(session).toString();

        return new ResponseEntity<String>(message, HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<String> handelException(IllegalMoveException e){
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }


    /**
     *
     * @param session
     * @return
     */
    private Long mapUserIdBySession(HttpSession session){
        //Получаем информацию из сессии
        Long userId = (Long) session.getAttribute("userId");

        //Если информации нет - создаем нового пользователя, и добавляем атрибут
        if (userId == null){
            User newUser = gameService.getNewUser();
            userId = newUser.getId();
            session.setAttribute("userId", userId);
        }

        return userId;
    }

}
