package ru.hse.chislius_server.game;

public interface BoardInterface {
    boolean openCard(int x, int y);
    int doMove();
    void skipMove();
    boolean canMove();
}
