package ru.hse.chislius_server.game.repository;

import org.springframework.stereotype.Component;
import ru.hse.chislius_server.game.entity.Game;

import java.util.HashMap;

@Component
public class GameRepository {
    private final HashMap<String, Game> games = new HashMap<>();

    public void add(String key, Game game) {
        games.put(key, game);
    }

    public Game get(String key) {
        return games.get(key);
    }
}
