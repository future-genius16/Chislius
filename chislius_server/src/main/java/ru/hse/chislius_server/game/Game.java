package ru.hse.chislius_server.game;

public class Game {
    public static void main(String[] args) {
        Board board = new Board();
        board.doMove();
        System.out.println(board);

        board.openCard(3, 3);
        System.out.println(board);

        board.openCard(2, 1);
        System.out.println(board);

        System.out.println(board.doMove());
        System.out.println(board);

        board.openCard(1, 0);
        System.out.println(board);

        board.openCard(0, 2);
        System.out.println(board);

        System.out.println(board.doMove());
        System.out.println(board);

    }
}
