package ru.hse.chislius_server.user;

import lombok.Data;
import lombok.ToString;
import ru.hse.chislius_server.room.model.AbstractRoom;

@Data
@ToString(exclude = "currentRoom")
public class User {
    private final String username;
    private final String password;
    private AbstractRoom currentRoom;
}
