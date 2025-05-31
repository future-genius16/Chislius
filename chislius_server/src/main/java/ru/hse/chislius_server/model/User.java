package ru.hse.chislius_server.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.hse.chislius_server.model.room.Room;

@Data
@ToString(of = "username")
@EqualsAndHashCode(of = "username")
public class User {
    private String username;
    private String password;
    private String token;
    private String sessionId;
    private Room currentRoom;

    public User(String username) {
        this.username = username;
    }
}
