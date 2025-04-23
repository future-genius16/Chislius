package ru.hse.chislius_server.game;

public class Game {
    public static void main(String[] args) {
        Board board = new Board();
        board.combineCards();
        System.out.println(board);

        board.flipCard(3, 3);
        System.out.println(board);

        board.flipCard(2, 1);
        System.out.println(board);

        System.out.println(board.combineCards());
        System.out.println(board);

        board.flipCard(1, 0);
        System.out.println(board);

        board.flipCard(0, 2);
        System.out.println(board);

        System.out.println(board.combineCards());
        System.out.println(board);

    }
}
