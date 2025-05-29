package ru.hse.chislius_server.dto.update;

import lombok.Data;
import ru.hse.chislius_server.model.User;
import ru.hse.chislius_server.model.room.Room;
import ru.hse.chislius_server.model.room.RoomState;

@Data
public class UpdateResponse {
    private final int state;
    private final UserResponse player;
    private final RoomUpdate data;

    public UpdateResponse(User user) {
        player = new UserResponse(user);
        int tmpState = 0;
        RoomUpdate tmpData = null;
        Room room = user.getCurrentRoom();
        if (room != null) {
            if (room.getState() == RoomState.WAIT) {
                tmpState = 1;
            } else if (room.getState() == RoomState.IN_PROGRESS) {
                if (room.getCurrentPlayer() != user) {
                    tmpState = 2;
                } else {
                    tmpState = 3;
                }
            } else if (room.getState() == RoomState.FINISH) {
                tmpState = 4;
            }
            tmpData = new RoomUpdate(room);
        }
        state = tmpState;
        data = tmpData;
    }
}
