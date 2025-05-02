package ru.hse.chislius_server.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Board implements BoardInterface {
    private static final int SIZE = 4;
    private final GameMode gameMode;
    private final Set<Card> openCards;
    private final List<Card> cards;
    private final LinkedList<Card> cardPool;
    private final List<Potion> potions;
    private final LinkedList<Potion> potionPool;

    public Board(GameMode gameMode) {
        this.gameMode = gameMode;

        cardPool = new LinkedList<>();
        generateCardsPool();

        cards = new ArrayList<>();
        for (int i = 0; i < SIZE * SIZE; i++) {
            cards.add(cardPool.pop());
        }

        openCards = new HashSet<>();

        potionPool = new LinkedList<>();
        generatePotionsPool();

        potions = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
            potions.add(potionPool.pop());
        }
    }

    public boolean openCard(int x, int y) {
        if (openCards.size() < 3) {
            Card card = cards.get(x * SIZE + y);
            if (card != null && !card.isOpen()) {
                card.setOpen(true);
                openCards.add(card);
                return true;
            }
        }
        return false;
    }

    public int doMove() {
        int sum = 0;
        Color color = null;
        for (Card card : openCards) {
            sum += card.getValue();
            if (color == null) {
                color = card.getColor();
            } else {
                color = color.combine(card.getColor());
            }
        }

        for (Potion potion : potions) {
            if (gameMode.equals(GameMode.EASY) || potion.getQuantity() == openCards.size()) {
                if (!gameMode.equals(GameMode.HARD) || potion.getColor().equals(color)) {
                    if (potion.getValue() == sum) {
                        dropOpenCards();
                        dropPotion(potion);
                        return potion.getValue();
                    }
                }
            }
        }
        flipOpenCards();
        return 0;
    }

    public void skipMove() {
        flipOpenCards();
    }

    public boolean canMove() {
        for (Potion potion : potions) {
            for (int i1 = 0; i1 < SIZE * SIZE; i1++) {
                Card card1 = cardPool.get(i1);
                if (card1 != null && card1.getValue() < potion.getValue() && checkColorNotUnknown(card1.getColor().combine(potion.getColor()))) {
                    for (int i2 = i1 + 1; i2 < SIZE * SIZE; i2++) {
                        Card card2 = cardPool.get(i2);
                        if (card2 != null) {
                            if (gameMode.equals(GameMode.EASY) || potion.getQuantity() == 2) {
                                if (card1.getValue() + card2.getValue() == potion.getValue() && (!gameMode.equals(GameMode.HARD) || card1.getColor().combine(card2.getColor()).equals(potion.getColor()))) {
                                    return true;
                                }
                            }
                            if (gameMode.equals(GameMode.EASY) || potion.getQuantity() == 3) {
                                for (int i3 = i2 + 1; i3 < SIZE * SIZE; i3++) {
                                    Card card3 = cardPool.get(i3);
                                    if (card3 != null && card1.getValue() + card2.getValue() + card3.getValue() == potion.getValue() && checkColorEquals(card1.getColor().combine(card2.getColor()).combine(card3.getColor()), potion.getColor())) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean checkColorEquals(Color left, Color right) {
        return !gameMode.equals(GameMode.HARD) || left.equals(right);
    }

    private boolean checkColorNotUnknown(Color color) {
        return !gameMode.equals(GameMode.HARD) || !color.equals(Color.UNKNOWN);
    }


    private void flipOpenCards() {
        for (Card card : openCards) {
            card.setOpen(false);
        }
        openCards.clear();
    }

    private void dropOpenCards() {
        for (int i = 0; i < SIZE * SIZE; i++) {
            Card card = cardPool.get(i);
            if (card != null && card.isOpen()) {
                dropCard(i);
            }
        }
        openCards.clear();
    }

    private void dropPotion(Potion potion) {
        potions.remove(potion);
        if (!potionPool.isEmpty()) {
            potions.add(potionPool.pop());
        }
    }

    private void dropCard(int i) {
        cards.remove(i);
        if (!cardPool.isEmpty()) {
            cards.add(i, cardPool.pop());
        } else {
            cards.add(i, null);
        }
    }

    private void generateCardsPool() {
        cardPool.add(new Card(1, Color.BLUE));
        cardPool.add(new Card(1, Color.BLUE));
        cardPool.add(new Card(2, Color.BLUE));
        cardPool.add(new Card(2, Color.BLUE));
        cardPool.add(new Card(2, Color.BLUE));
        cardPool.add(new Card(2, Color.BLUE));
        cardPool.add(new Card(3, Color.BLUE));
        cardPool.add(new Card(3, Color.BLUE));
        cardPool.add(new Card(3, Color.BLUE));
        cardPool.add(new Card(4, Color.BLUE));
        cardPool.add(new Card(4, Color.BLUE));
        cardPool.add(new Card(5, Color.BLUE));
        cardPool.add(new Card(5, Color.BLUE));
        cardPool.add(new Card(6, Color.BLUE));
        cardPool.add(new Card(1, Color.YELLOW));
        cardPool.add(new Card(1, Color.YELLOW));
        cardPool.add(new Card(2, Color.YELLOW));
        cardPool.add(new Card(2, Color.YELLOW));
        cardPool.add(new Card(2, Color.YELLOW));
        cardPool.add(new Card(2, Color.YELLOW));
        cardPool.add(new Card(3, Color.YELLOW));
        cardPool.add(new Card(3, Color.YELLOW));
        cardPool.add(new Card(3, Color.YELLOW));
        cardPool.add(new Card(4, Color.YELLOW));
        cardPool.add(new Card(4, Color.YELLOW));
        cardPool.add(new Card(5, Color.YELLOW));
        cardPool.add(new Card(5, Color.YELLOW));
        cardPool.add(new Card(6, Color.YELLOW));
        cardPool.add(new Card(1, Color.RED));
        cardPool.add(new Card(1, Color.RED));
        cardPool.add(new Card(2, Color.RED));
        cardPool.add(new Card(2, Color.RED));
        cardPool.add(new Card(2, Color.RED));
        cardPool.add(new Card(2, Color.RED));
        cardPool.add(new Card(3, Color.RED));
        cardPool.add(new Card(3, Color.RED));
        cardPool.add(new Card(3, Color.RED));
        cardPool.add(new Card(4, Color.RED));
        cardPool.add(new Card(4, Color.RED));
        cardPool.add(new Card(5, Color.RED));
        cardPool.add(new Card(6, Color.RED));
        cardPool.add(new Card(6, Color.RED));

        Collections.shuffle(cardPool);
    }

    private void generatePotionsPool() {
        potionPool.add(new Potion(4, Color.GREEN, 2));
        potionPool.add(new Potion(6, Color.GREEN, 2));
        potionPool.add(new Potion(7, Color.GREEN, 2));
        potionPool.add(new Potion(8, Color.GREEN, 2));
        potionPool.add(new Potion(9, Color.GREEN, 2));
        potionPool.add(new Potion(10, Color.GREEN, 3));
        potionPool.add(new Potion(3, Color.ORANGE, 2));
        potionPool.add(new Potion(5, Color.ORANGE, 2));
        potionPool.add(new Potion(6, Color.ORANGE, 2));
        potionPool.add(new Potion(8, Color.ORANGE, 3));
        potionPool.add(new Potion(9, Color.ORANGE, 3));
        potionPool.add(new Potion(10, Color.ORANGE, 3));
        potionPool.add(new Potion(4, Color.VIOLET, 2));
        potionPool.add(new Potion(5, Color.VIOLET, 2));
        potionPool.add(new Potion(7, Color.VIOLET, 3));
        potionPool.add(new Potion(8, Color.VIOLET, 2));
        potionPool.add(new Potion(9, Color.VIOLET, 3));
        potionPool.add(new Potion(10, Color.VIOLET, 2));

        Collections.shuffle(potionPool);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(potions);
        sb.append("\n");
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                sb.append(cards.get(i * SIZE + j));
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}