package ru.hse.chislius_server.room.dto;

import lombok.Data;
import ru.hse.chislius_server.room.model.AbstractRoom;

import java.util.List;

@Data
public class RoomsUpdateResponse {
    public RoomsUpdateResponse(List<AbstractRoom> rooms) {
        StringBuilder sb = new StringBuilder();
        for (AbstractRoom room : rooms) {
            sb.append("<br>");
            sb.append(room);
        }
        sb.append("<br>");
        state = sb.toString();
    }

    private final String state;
}
