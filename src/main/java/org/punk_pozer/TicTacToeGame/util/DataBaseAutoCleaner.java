package org.punk_pozer.TicTacToeGame.util;


import org.punk_pozer.TicTacToeGame.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@EnableAsync
public class DataBaseAutoCleaner {

    private  final BoardRepository boardRepository;

    @Autowired
    public DataBaseAutoCleaner(BoardRepository boardRepository){
        this.boardRepository = boardRepository;
    }

    @Async
    @Scheduled(cron = "${util.autocleaner.cron}")
    public void deleteUnusedBoards(){
        int deletedCount = boardRepository.deleteOldBoards(LocalDateTime.now().minusDays(1));
        System.out.println("Clean garbage, deleted " + deletedCount + " rows");
    }
}
