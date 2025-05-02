package ru.hse.chislius_server.dto.room;

import lombok.Data;
import ru.hse.chislius_server.model.room.Room;

import java.util.List;

@Data
public class RoomsUpdateResponse {
    public RoomsUpdateResponse(List<Room> rooms) {
        StringBuilder sb = new StringBuilder();
        for (Room room : rooms) {
            sb.append("<br>");
            sb.append(room);
        }
        sb.append("<br>");
        state = sb.toString();
    }

    private final String state;
}
