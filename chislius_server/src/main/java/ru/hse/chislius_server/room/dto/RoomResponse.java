package ru.hse.chislius_server.room.dto;

import lombok.Data;
import ru.hse.chislius_server.room.model.AbstractRoom;

@Data
public class RoomResponse {
    private final String name;
    private final String roomId;

    public RoomResponse(AbstractRoom room) {
        this.name = "";
        this.roomId = room.getCode();
    }
}
