package ru.hse.chislius_server.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Board implements BoardInterface {
    private static final int SIZE = 4;
    private final Set<Card> openCards;
    private final List<Card> cards;
    private final LinkedList<Card> cardPool;
    private final List<Potion> potions;
    private final LinkedList<Potion> potionPool;

    public Board() {
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
        for (Card card : openCards) {
            sum += card.getValue();
        }
        for (Potion potion : potions) {
            if (potion.getQuantity() == openCards.size() && potion.getValue() == sum) {
                dropOpenCards();
                dropPotion(potion);
                return potion.getValue();
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
            if (potion.getQuantity() == 2) {
                for (int i1 = 0; i1 < SIZE * SIZE; i1++) {
                    Card card1 = cardPool.get(i1);
                    if (card1 != null && card1.getValue() < potion.getValue()) {
                        for (int i2 = i1 + 1; i2 < SIZE * SIZE; i2++) {
                            Card card2 = cardPool.get(i2);
                            if (card2 != null && card1.getValue() + card2.getValue() == potion.getValue()) {
                                return true;
                            }
                        }
                    }
                }
            } else if (potion.getQuantity() == 3) {
                for (int i1 = 0; i1 < SIZE * SIZE; i1++) {
                    Card card1 = cardPool.get(i1);
                    if (card1 != null && card1.getValue() < potion.getValue()) {
                        for (int i2 = i1 + 1; i2 < SIZE * SIZE; i2++) {
                            Card card2 = cardPool.get(i2);
                            if (card2 != null && card1.getValue() + card2.getValue() < potion.getValue()) {
                                for (int i3 = i2 + 1; i3 < SIZE * SIZE; i3++) {
                                    Card card3 = cardPool.get(i3);
                                    if (card3 != null && card1.getValue() + card2.getValue() + card3.getValue() == potion.getValue()) {
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
        cardPool.add(new Card(1, 1));
        cardPool.add(new Card(1, 1));
        cardPool.add(new Card(1, 2));
        cardPool.add(new Card(1, 2));
        cardPool.add(new Card(1, 2));
        cardPool.add(new Card(1, 2));
        cardPool.add(new Card(1, 3));
        cardPool.add(new Card(1, 3));
        cardPool.add(new Card(1, 3));
        cardPool.add(new Card(1, 4));
        cardPool.add(new Card(1, 4));
        cardPool.add(new Card(1, 5));
        cardPool.add(new Card(1, 5));
        cardPool.add(new Card(1, 6));
        cardPool.add(new Card(2, 1));
        cardPool.add(new Card(2, 1));
        cardPool.add(new Card(2, 2));
        cardPool.add(new Card(2, 2));
        cardPool.add(new Card(2, 2));
        cardPool.add(new Card(2, 2));
        cardPool.add(new Card(2, 3));
        cardPool.add(new Card(2, 3));
        cardPool.add(new Card(2, 3));
        cardPool.add(new Card(2, 4));
        cardPool.add(new Card(2, 4));
        cardPool.add(new Card(2, 5));
        cardPool.add(new Card(2, 5));
        cardPool.add(new Card(2, 6));
        cardPool.add(new Card(3, 1));
        cardPool.add(new Card(3, 1));
        cardPool.add(new Card(3, 1));
        cardPool.add(new Card(3, 2));
        cardPool.add(new Card(3, 2));
        cardPool.add(new Card(3, 2));
        cardPool.add(new Card(3, 3));
        cardPool.add(new Card(3, 3));
        cardPool.add(new Card(3, 3));
        cardPool.add(new Card(3, 4));
        cardPool.add(new Card(3, 4));
        cardPool.add(new Card(3, 5));
        cardPool.add(new Card(3, 5));
        cardPool.add(new Card(3, 6));

        Collections.shuffle(cardPool);
    }

    private void generatePotionsPool() {
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