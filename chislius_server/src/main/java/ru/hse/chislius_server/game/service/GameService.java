package ru.hse.chislius_server.game.service;

import ru.hse.chislius_server.game.entity.Game;
import ru.hse.chislius_server.game.models.GameMode;

public interface GameService {
    Game createGame(String key, GameMode gameMode);

    boolean openCard(Game game, int i);

    int doMove(Game game);

    void skipMove(Game game);

    boolean canMove(Game game);
}
