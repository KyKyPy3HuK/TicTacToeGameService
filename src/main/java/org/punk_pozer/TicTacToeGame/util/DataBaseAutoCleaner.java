package org.punk_pozer.TicTacToeGame.util;


import org.punk_pozer.TicTacToeGame.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableAsync
public class DataBaseAutoCleaner {



    @Autowired
    public DataBaseAutoCleaner(GameRepository gameRepository){
        System.out.println("DBAC start");
    }

    @Async
    @Scheduled(cron = "${util.autocleaner.cron}")
    public void deleteUnusedBoards(){
        System.out.println("Clean garbage...");
    }
}
