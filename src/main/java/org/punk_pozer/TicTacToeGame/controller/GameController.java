package org.punk_pozer.TicTacToeGame.controller;

import jakarta.servlet.http.HttpSession;
import org.punk_pozer.TicTacToeGame.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     *
     * @return
     */
    @GetMapping("")
    public ResponseEntity<?> getGameById(){
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

        String message;

        if (session.getAttribute("boardId") == null){
            session.setAttribute("boardId", board_counter++);

        }
        else{

        }

        message = ((Integer)session.getAttribute("boardId")).toString();


        return new ResponseEntity<String>(message, HttpStatus.OK);
    }

}
