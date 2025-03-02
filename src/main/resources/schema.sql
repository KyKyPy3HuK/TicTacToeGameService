-- Создание последовательностей
CREATE SEQUENCE BOARDS_SEQ START WITH 1 INCREMENT BY 50;
CREATE SEQUENCE MOVES_SEQ START WITH 1 INCREMENT BY 50;

-- Создание таблицы boards
CREATE TABLE boards (
    id BIGINT DEFAULT NEXT VALUE FOR BOARDS_SEQ NOT NULL PRIMARY KEY,
    is_player_first BOOLEAN NOT NULL,
    last_move_time TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('COMPUTER_WIN', 'DRAW', 'FINISHED', 'PLAYER_WIN', 'STARTED'))
);

-- Создание таблицы move
CREATE TABLE moves (
    id BIGINT DEFAULT NEXT VALUE FOR MOVES_SEQ NOT NULL PRIMARY KEY,
    is_player_move BOOLEAN NOT NULL,
    number INTEGER NOT NULL,
    position INTEGER NOT NULL,
    board_id BIGINT NOT NULL,
    CONSTRAINT fk_moves_board FOREIGN KEY (board_id) REFERENCES boards(id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Создание индексов
create index idx_boards_id on boards (id); -- Стандартный индекс
create index idx_moves_board_id on moves (board_id); -- Для ускоренного поиска ходов по доске
