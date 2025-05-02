package ru.hse.chislius_server.dto.room;

import lombok.Data;
import ru.hse.chislius_server.model.room.Room;

@Data
public class RoomResponse {
    private final String name;
    private final String roomId;

    public RoomResponse(Room room) {
        this.name = "";
        this.roomId = room.getCode();
    }
}
