package ru.hse.chislius_server.model.room;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.hse.chislius_server.model.User;

import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(of = {"type", "capacity"})
public class Room {
    private final Set<User> users = new HashSet<>();
    private final RoomType type;
    private final int capacity;
    private RoomState state;
    private String code;
}
