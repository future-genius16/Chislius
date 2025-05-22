package ru.hse.chislius_server.game.entity;

import lombok.RequiredArgsConstructor;
import ru.hse.chislius_server.game.models.GameMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class Game {
    public final String key;
    public final int size;
    public final GameMode gameMode;
    public final Set<Card> openCards = new HashSet<>();
    public final List<Card> cards = new ArrayList<>();
    public final LinkedList<Card> cardsPool;
    public final List<Potion> potions = new ArrayList<>();
    public final LinkedList<Potion> potionsPool;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(potions);
        sb.append("\n");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                sb.append(cards.get(i * size + j));
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}