package ru.hse.chislius_server.model.room;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.hse.chislius_server.game.entity.Game;
import ru.hse.chislius_server.game.models.GameMode;
import ru.hse.chislius_server.model.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

@Data
@EqualsAndHashCode(of = {"type", "capacity"})
public class Room {
    private final Set<User> users = new HashSet<>();
    private final Map<User, Integer> scores = new HashMap<>();
    private final LinkedList<User> players = new LinkedList<>();
    private User currentPlayer;
    private final RoomType type;
    private final int capacity;
    private RoomState state;
    private String code;
    private Game game;
    private final GameMode mode;
}
