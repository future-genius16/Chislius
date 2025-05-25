package ru.hse.chislius_server.dto.update;

import lombok.Data;
import ru.hse.chislius_server.game.entity.Game;
import ru.hse.chislius_server.model.room.Room;
import ru.hse.chislius_server.model.room.RoomState;
import ru.hse.chislius_server.model.room.RoomType;

import java.util.List;

@Data
public final class RoomUpdate {
    private final String code;
    private final boolean open;
    private final int capacity;
    private final List<UserResponse> players;
    private final UserResponse currentPlayer;
    private final GameResponse board;

    public RoomUpdate(Room room) {
        this.code = room.getCode();
        this.open = room.getType() == RoomType.PUBLIC;
        this.capacity = room.getCapacity();
        if (room.getState() == RoomState.WAIT) {
            this.players = room.getUsers().stream().map(UserResponse::new).toList();
            currentPlayer = null;
        } else {
            this.players = room.getPlayers().stream().map(UserResponse::new).toList();
            currentPlayer = new UserResponse(room.getCurrentPlayer());
        }
        Game game = room.getGame();
        if (game != null) {
            this.board = new GameResponse(game);
        } else {
            this.board = null;
        }
    }
}