package ru.hse.chislius_server.game.service;

import ru.hse.chislius_server.game.entity.Game;
import ru.hse.chislius_server.game.models.GameMode;
import ru.hse.chislius_server.game.models.GamePresentation;

public interface GameService {
    Game createGame(String key, GameMode gameMode);

    Game getRoom(String key);

    boolean openCard(Game game, int x, int y);

    int doMove(Game game);

    void skipMove(Game game);

    boolean canMove(Game game);

    GamePresentation getGamePresentation(Game game);
}
