package ru.hse.chislius_server.game;

public interface GameService {
    Game createGame(GameMode gameMode);
    boolean openCard(Game game, int x, int y);
    int doMove(Game game);
    void skipMove(Game game);
    boolean canMove(Game game);
    GamePresentation getGamePresentation(Game game);
}
