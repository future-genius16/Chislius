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
    private final Set<Long> userIds = new HashSet<>();
    private final Map<Long, Integer> scores = new HashMap<>();
    private final LinkedList<Long> playerIds = new LinkedList<>();
    private long currentPlayerId;
    private final RoomType type;
    private final int capacity;
    private RoomState state;
    private String code;
    private Game game;
    private final GameMode mode;
}
