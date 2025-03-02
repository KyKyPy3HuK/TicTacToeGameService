package org.punk_pozer.TicTacToeGame.service;

import org.punk_pozer.TicTacToeGame.exception.IllegalMoveException;
import org.punk_pozer.TicTacToeGame.exception.IllegalMoveUndoException;
import org.punk_pozer.TicTacToeGame.model.Board;
import org.punk_pozer.TicTacToeGame.model.GameStatus;
import org.punk_pozer.TicTacToeGame.model.Move;
import org.punk_pozer.TicTacToeGame.repository.BoardRepository;
import org.punk_pozer.TicTacToeGame.repository.MoveRepository;
import org.punk_pozer.TicTacToeGame.util.ComputerAi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class GameService {

    private static final Random random = new Random();

    private final BoardRepository boardRepository;

    private final MoveRepository moveRepository;

    @Autowired
    public GameService(BoardRepository boardRepository,
                       MoveRepository moveRepository){
        this.boardRepository = boardRepository;
        this.moveRepository = moveRepository;
    }

    /**
     * Получить доску по идентификатору
     * @param id идентификатор доски
     * @return доска - если найдена, иначе Optional.empty()
     * */
    @Transactional(readOnly = true)
    public Optional<Board> getBoardById(Long id) {
        Optional<Board> board = boardRepository.getBoardById(id);
        return board;
    }


    /**
     * Создать и получить новую доску
     * @param isPlayerFirst определяет субъекта, который будет делать первый ход,
     *                     если true то первым ходит игрок, иначе (false) первым ходит машина
     * @return возвращает новую доску, и если первой ходит машина (isPlayerFirst == false)
     * то на ней будет ее первый ход
     */
    public Board getNewBoard(boolean isPlayerFirst){

        //Создание новой доски
        Board newBoard = new Board();
        newBoard.setUserFirst(isPlayerFirst);
        newBoard.setLastMoveTime(LocalDateTime.now());
        newBoard.setStatus(GameStatus.STARTED);

        //Первый ход компьютера
        if (!isPlayerFirst){
            int compMovePos = ComputerAi.getRandMovePos(newBoard.getState(),isPlayerFirst);
            Move compMove = new Move();
            compMove.setPlayerMove(false);
            compMove.setBoard(newBoard);
            compMove.setNumber(0);
            compMove.setPosition(compMovePos);
            newBoard.getMoves().add(compMove);
        }

        boardRepository.save(newBoard);

        return newBoard;
    }

    /**
     * Устанавливает статус доски "FINISHED" - игра закончена досрочно
     * @param board доска, статус которой будет изменен
     */
    public void finalizeBoard(Board board){
        board.setStatus(GameStatus.FINISHED);
        boardRepository.save(board);
    }

    /**
     * Сделать ход на доске
     * @param board доска на которой будет произведен ход
     * @param position координаты хода
     * @return возвращает обновленную доску содержащую ход человека и ответный ход машины,
     * также может поменять статус доски при выполнении условий выйгрыша или ничьи
     * @throws IllegalMoveException если ход не удовлетворяет условия корректности
     */
    public Board makeMove(Board board, int position) throws IllegalMoveException {

        //Проверка хода на корректность
        if(verifyMove(board, position)){
            //Выполнение хода на доске
            Move move = new Move();
            move.setBoard(board);
            move.setNumber(board.getMoves().size());
            move.setPosition(position);
            move.setPlayerMove(true) ;
            board.getMoves().add(move);
            //Обновление времени последнего хода
            board.setLastMoveTime(LocalDateTime.now());
        };

        //Проверка на выйгрыш человека
        if (checkWin(board, true)){
            board.setStatus(GameStatus.PLAYER_WIN);

        }
        //Проверка на ничью
        else if (checkDraw(board)){
            board.setStatus(GameStatus.DRAW);
        }
        //Ход машины
        else {
            //Выбор позиции хода
            int compMovePos = ComputerAi.getRandMovePos(board.getState(), board.isUserFirst());
            Move compMove = new Move();
            compMove.setBoard(board);
            compMove.setNumber(board.getMoves().size());
            compMove.setPosition(compMovePos);
            compMove.setPlayerMove(false) ;
            board.getMoves().add(compMove);

            //Проверка на выйгрыш машины
            if (checkWin(board,false)){
                board.setStatus(GameStatus.COMPUTER_WIN);
            }
            //Проверка на ничью
            else if (checkDraw(board)) {
                board.setStatus(GameStatus.DRAW);
            }
        }

        boardRepository.save(board);

        return board;
    }


    /**
     * Отмена последнего хода игрока и последнего хода компьютера
     * @param board доска, ходы на которой будут отменяться
     * @return
     */
    public Board undoMove(Board board) throws IllegalMoveUndoException {

        if (!(board.getStatus() == GameStatus.STARTED)){
            throw new IllegalMoveUndoException("Board status = "
                    + board.getStatus().toString()
                    + " not equal to \""
                    + GameStatus.STARTED.toString()+"\"" +
                    " to undo a move, use  /api/game/new  to create a new board");
        }

        int movesCount = board.getMoves().size();
        if (movesCount < 1){
            throw new IllegalMoveUndoException("No moves on the board to undo");
        }

        //Если кол-во ходов = 1, то партия началась с хода машины, а игрок еще не делал ход
        //Необходимо удалить ход машины, затем машина сделает ход заново
        if (movesCount == 1){
            //Удаляем единственный ход
            board.getMoves().remove(0);
            //Добавляем обратно другой первый ход машины
            int compMovePos = ComputerAi.getRandMovePos(board.getState(),board.isUserFirst());
            Move compMove = new Move();
            compMove.setPlayerMove(false);
            compMove.setBoard(board);
            compMove.setNumber(0);
            compMove.setPosition(compMovePos);
            board.getMoves().add(compMove);
        }
        //Если кол-во ходов >= 2, то удаляем 2 последних хода: последний ход машины и игрока
        else{

            List<Move> removeList =  board.getMoves().stream().sorted(Comparator.comparingInt(Move::getNumber).reversed()).limit(2).toList();
            for (int i = 0; i < 2; i++) {
                board.getMoves().remove(removeList.get(i));
            }
        }

        boardRepository.save(board);

        return board;
    }

    /**
     * Проверка хода на корректности
     * @param board доска на которой проверяется ход
     * @param position позиция хода на доске
     * @return возвращает true если ход удовлетворяет условиям корректности
     * @throws IllegalMoveException если ход не удовлетворяет условиям корректности
     */
    private boolean verifyMove(final Board board, final int position) throws IllegalMoveException{

        //Проверка статуса доски
        if(board.getStatus() != GameStatus.STARTED){
            throw new IllegalMoveException("Board status = "
                    + board.getStatus().toString()
                    + " not equal to \""
                    + GameStatus.STARTED.toString()+"\"" +
                    " to make a move, use  /api/game/new  to create a new board");
        }

        //Проверка на диапазон координат хода
        char[][] boardState = board.getState();
        int x = position % 3;
        int y = position / 3;
        if ((x < 0 || x > 2) || (y < 0 || y > 2)){
            throw new IllegalMoveException("Illegal move pos = " + position
                    + " (x = " + x + ", y = " + y + ")"
                    + ", the position must be in the range from 0 to 8 (x = 0..2, y = 0..2)");
        }

        //Проверка на занятость ячейки
        if(boardState[y][x] != Board.FREE){
            throw new IllegalMoveException("Illegal move pos = " + position
                    + " (x = " + x + ", y = " + y + ")"
                    + " - this cell is already taken, try another move pos");
        }

        return true;
    }

    /**
     * Проверка условий победы
     * @param board доска на которой будут проверяться условия
     * @param isPlayerCheck если true, то будут проверяться ходы игрока, иначе (false) ходы машины
     * @return true если условия победы выполняются для игрока/машины
     * (в зависимости от аргумента isPlayerCheck), иначе false
     */
    private static boolean checkWin(final Board board, final boolean isPlayerCheck){

        //Определение субъекта проверки на победу (Машина/Человек)
        char mark;
        if (board.isUserFirst()){
            mark = (isPlayerCheck)? Board.CROSS : Board.ZERO;
        } else {
            mark = (isPlayerCheck  )? Board.ZERO : Board.CROSS;
        }

        // Проверка строк и столбцов
        char[][] boardState = board.getState();
        for (int i = 0; i < 3; i++)
            if ((boardState[i][0] == mark && boardState[i][1] == mark &&
                    boardState[i][2] == mark) ||
                    (boardState[0][i] == mark && boardState[1][i] == mark &&
                            boardState[2][i] == mark)){
                return true;
            }

        // Проверка диагоналей
        if ((boardState[0][0] == mark && boardState[1][1] == mark &&
                boardState[2][2] == mark) ||
                (boardState[2][0] == mark && boardState[1][1] == mark &&
                        boardState[0][2] == mark)){
            return true;
        }

        return false;
    }


    /**
     * Проверка на условия ничьи
     * @param board доска на которой будут проверяться условия
     * @return true если все ячейки заняты, иначе false
     */
    private static boolean checkDraw(final Board board){
        char[][] boardState = board.getState();
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 3; col++)
                if (boardState[row][col] == Board.FREE){
                    return false;
                }

        return true;
    }

}
