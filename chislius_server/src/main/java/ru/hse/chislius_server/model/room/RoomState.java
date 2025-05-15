package ru.hse.chislius_server.model.room;

public enum RoomState {
    /**
     * Ожидание игроков
     */
    WAIT,
    
    /**
     * Игра началась
     */
    IN_PROGRESS,

    /**
     * Игра завершилась
     */
    FINISH,
    
    /**
     * Комната удалена
     */
    DELETE
}

