package ru.hse.chislius_server.model;

import lombok.Data;
import lombok.ToString;
import ru.hse.chislius_server.model.room.Room;

@Data
@ToString(exclude = "currentRoom")
public class User {
    private final String username;
    private final String password;
    private Room currentRoom;
}
