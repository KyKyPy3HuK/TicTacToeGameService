package org.punk_pozer.TicTacToeGame.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_users_id", columnList = "id")
        })
public class User {
    @Id
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Board> boards = new ArrayList<>();
}
