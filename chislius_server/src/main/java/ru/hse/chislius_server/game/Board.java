package ru.hse.chislius_server.game;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Board {
    private static final int SIZE = 4;
    private final Card[][] data;
    private final LinkedList<Card> cards;
    private final List<Potion> potions;
    private final LinkedList<Potion> potionPool;

    public Board() {
        cards = new LinkedList<>();
        generateCards();

        data = new Card[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                data[i][j] = cards.pop();
            }
        }

        potionPool = new LinkedList<>();
        generatePotions();

        potions = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
            potions.add(potionPool.pop());
        }
    }

    public void flipCard(int x, int y) {
        if (data[x][y] != null && !data[x][y].isVisible()) {
            data[x][y].setVisible(true);
        }
    }

    public void dropVisibleCards() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (data[i][j] != null && data[i][j].isVisible()) {
                    dropCard(i, j);
                }
            }
        }
    }

    private void dropCard(int x, int y) {
        if (data[x][y] != null && data[x][y].isVisible()) {
            if (!cards.isEmpty()) {
                data[x][y] = cards.pop();
            } else {
                data[x][y] = null;
            }
        }
    }

    public boolean combineCards() {
        int sum = 0;
        int count = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (data[i][j] != null && data[i][j].isVisible()) {
                    sum += data[i][j].getValue();
                    count++;
                }
            }
        }
        for (Potion potion : potions) {
            if (potion.getQuantity() == count && potion.getValue() == sum) {
                dropVisibleCards();
                potions.remove(potion);
                if (!potionPool.isEmpty()) {
                    potions.add(potionPool.pop());
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(potions);
        sb.append("\n");
        for (Card[] row : data) {
            for (Card card : row) {
                sb.append(card);
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private void generateCards() {
        cards.add(new Card(1, 1));
        cards.add(new Card(1, 1));
        cards.add(new Card(1, 2));
        cards.add(new Card(1, 2));
        cards.add(new Card(1, 2));
        cards.add(new Card(1, 2));
        cards.add(new Card(1, 3));
        cards.add(new Card(1, 3));
        cards.add(new Card(1, 3));
        cards.add(new Card(1, 4));
        cards.add(new Card(1, 4));
        cards.add(new Card(1, 5));
        cards.add(new Card(1, 5));
        cards.add(new Card(1, 6));
        cards.add(new Card(2, 1));
        cards.add(new Card(2, 1));
        cards.add(new Card(2, 2));
        cards.add(new Card(2, 2));
        cards.add(new Card(2, 2));
        cards.add(new Card(2, 2));
        cards.add(new Card(2, 3));
        cards.add(new Card(2, 3));
        cards.add(new Card(2, 3));
        cards.add(new Card(2, 4));
        cards.add(new Card(2, 4));
        cards.add(new Card(2, 5));
        cards.add(new Card(2, 5));
        cards.add(new Card(2, 6));
        cards.add(new Card(3, 1));
        cards.add(new Card(3, 1));
        cards.add(new Card(3, 1));
        cards.add(new Card(3, 2));
        cards.add(new Card(3, 2));
        cards.add(new Card(3, 2));
        cards.add(new Card(3, 3));
        cards.add(new Card(3, 3));
        cards.add(new Card(3, 3));
        cards.add(new Card(3, 4));
        cards.add(new Card(3, 4));
        cards.add(new Card(3, 5));
        cards.add(new Card(3, 5));
        cards.add(new Card(3, 6));

        Collections.shuffle(cards);
    }

    private void generatePotions() {
        potionPool.add(new Potion(1, 4, 2));
        potionPool.add(new Potion(1, 6, 2));
        potionPool.add(new Potion(1, 7, 2));
        potionPool.add(new Potion(1, 8, 2));
        potionPool.add(new Potion(1, 9, 2));
        potionPool.add(new Potion(1, 10, 3));
        potionPool.add(new Potion(2, 3, 2));
        potionPool.add(new Potion(2, 5, 2));
        potionPool.add(new Potion(2, 6, 2));
        potionPool.add(new Potion(2, 8, 3));
        potionPool.add(new Potion(2, 9, 3));
        potionPool.add(new Potion(2, 10, 3));
        potionPool.add(new Potion(3, 4, 2));
        potionPool.add(new Potion(3, 5, 2));
        potionPool.add(new Potion(3, 7, 3));
        potionPool.add(new Potion(3, 8, 2));
        potionPool.add(new Potion(3, 9, 3));
        potionPool.add(new Potion(3, 10, 2));
        Collections.shuffle(potionPool);
    }
}
