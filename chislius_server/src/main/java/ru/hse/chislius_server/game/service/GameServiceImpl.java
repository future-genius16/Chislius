package ru.hse.chislius_server.game.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.hse.chislius_server.game.entity.Card;
import ru.hse.chislius_server.game.entity.Game;
import ru.hse.chislius_server.game.entity.Potion;
import ru.hse.chislius_server.game.models.Color;
import ru.hse.chislius_server.game.models.GameMode;
import ru.hse.chislius_server.game.models.GamePresentation;
import ru.hse.chislius_server.game.repository.GameRepository;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;

    private static final int BOARD_SIZE = 4;
    private static final int POTIONS_SIZE = 3;
    private static final int MAX_OPEN_CARD = 3;

    @Value("${config.game.wrong-move-cost}")
    private int WRONG_MOVE_COST;

    public Game createGame(String key, GameMode gameMode) {
        if (key == null || key.isEmpty() || gameRepository.get(key) != null) {
            return null;
        }
        Game game = new Game(key, BOARD_SIZE, gameMode, generateCardsPool(), generatePotionsPool());
        for (int i = 0; i < game.size * game.size; i++) {
            game.cards.add(game.cardsPool.pop());
        }
        for (int i = 0; i < POTIONS_SIZE; i++) {
            game.potions.add(game.potionsPool.pop());
        }
        gameRepository.add(key, game);
        sendChangesToPlayers(game);
        return game;
    }

    public Game getGame(String key) {
        return gameRepository.get(key);
    }

    public boolean openCard(Game game, int y, int x) {
        if (game.openCards.size() < MAX_OPEN_CARD) {
            Card card = game.cards.get(y * game.size + x);
            if (card != null && !card.isOpen()) {
                card.setOpen(true);
                game.openCards.add(card);
                sendChangesToPlayers(game);
                return true;
            }
        }
        return false;
    }

    public int doMove(Game game) {
        int sum = 0;
        Color color = null;
        for (Card card : game.openCards) {
            sum += card.getValue();
            if (color == null) {
                color = card.getColor();
            } else {
                color = color.combine(card.getColor());
            }
        }

        for (Potion potion : game.potions) {
            if (game.gameMode.equals(GameMode.EASY) || potion.getQuantity() == game.openCards.size()) {
                if (checkColorEquals(game, potion.getColor(), color)) {
                    if (potion.getValue() == sum) {
                        dropOpenCards(game);
                        dropPotion(game, potion);
                        sendChangesToPlayers(game);
                        return potion.getValue();
                    }
                }
            }
        }
        flipOpenCards(game);
        sendChangesToPlayers(game);
        return WRONG_MOVE_COST;
    }

    public void skipMove(Game game) {
        flipOpenCards(game);
        sendChangesToPlayers(game);
    }

    public boolean canMove(Game game) {
        for (Potion potion : game.potions) {
            for (int i1 = 0; i1 < game.cards.size(); i1++) {
                Card card1 = game.cards.get(i1);
                if (card1 != null && card1.getValue() < potion.getValue() && checkColorNotUnknown(game, card1.getColor().combine(potion.getColor()))) {
                    for (int i2 = i1 + 1; i2 < game.cards.size(); i2++) {
                        Card card2 = game.cards.get(i2);
                        if (card2 != null) {
                            if (game.gameMode.equals(GameMode.EASY) || potion.getQuantity() == 2) {
                                if (card1.getValue() + card2.getValue() == potion.getValue() && checkColorEquals(game, card1.getColor().combine(card2.getColor()), potion.getColor())) {
                                    return true;
                                }
                            }
                            if (game.gameMode.equals(GameMode.EASY) || potion.getQuantity() == 3) {
                                for (int i3 = i2 + 1; i3 < game.cards.size(); i3++) {
                                    Card card3 = game.cards.get(i3);
                                    if (card3 != null && card1.getValue() + card2.getValue() + card3.getValue() == potion.getValue() && checkColorEquals(game, card1.getColor().combine(card2.getColor()).combine(card3.getColor()), potion.getColor())) {
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

    public GamePresentation getGamePresentation(Game game) {
        return new GamePresentation(game.gameMode, List.copyOf(game.potions), List.copyOf(game.cards));
    }

    private void sendChangesToPlayers(Game game) {
        String key = game.key;
        System.out.println("key=" + key + ": " + getGamePresentation(game).toString());
    }


    private boolean checkColorEquals(Game game, Color left, Color right) {
        return !game.gameMode.equals(GameMode.HARD) || left.equals(right);
    }

    private boolean checkColorNotUnknown(Game game, Color color) {
        return !game.gameMode.equals(GameMode.HARD) || !color.equals(Color.UNKNOWN);
    }


    private void flipOpenCards(Game game) {
        for (Card card : game.openCards) {
            card.setOpen(false);
        }
        game.openCards.clear();
    }

    private void dropOpenCards(Game game) {
        for (int i = 0; i < game.cards.size(); i++) {
            Card card = game.cards.get(i);
            if (card != null && card.isOpen()) {
                dropCard(game, i);
            }
        }
        game.openCards.clear();
    }

    private void dropPotion(Game game, Potion potion) {
        game.potions.remove(potion);
        if (!game.potionsPool.isEmpty()) {
            game.potions.add(game.potionsPool.pop());
        }
    }

    private void dropCard(Game game, int i) {
        game.cards.remove(i);
        if (!game.cardsPool.isEmpty()) {
            game.cards.add(i, game.cardsPool.pop());
        } else {
            game.cards.add(i, null);
        }
    }

    private static void addMultipleCards(LinkedList<Card> cards, int value, Color color, int quantity) {
        for (int i = 0; i < quantity; i++) {
            cards.add(new Card(value, color));
        }
    }

    private static LinkedList<Card> generateCardsPool() {
        LinkedList<Card> cardsPool = new LinkedList<>();
        addMultipleCards(cardsPool, 1, Color.BLUE, 2);
        addMultipleCards(cardsPool, 2, Color.BLUE, 4);
        addMultipleCards(cardsPool, 3, Color.BLUE, 3);
        addMultipleCards(cardsPool, 4, Color.BLUE, 2);
        addMultipleCards(cardsPool, 5, Color.BLUE, 2);
        addMultipleCards(cardsPool, 6, Color.BLUE, 1);

        addMultipleCards(cardsPool, 1, Color.YELLOW, 2);
        addMultipleCards(cardsPool, 2, Color.YELLOW, 4);
        addMultipleCards(cardsPool, 3, Color.YELLOW, 3);
        addMultipleCards(cardsPool, 4, Color.YELLOW, 2);
        addMultipleCards(cardsPool, 5, Color.YELLOW, 2);
        addMultipleCards(cardsPool, 6, Color.YELLOW, 1);

        addMultipleCards(cardsPool, 1, Color.RED, 2);
        addMultipleCards(cardsPool, 2, Color.RED, 4);
        addMultipleCards(cardsPool, 3, Color.RED, 3);
        addMultipleCards(cardsPool, 4, Color.RED, 2);
        addMultipleCards(cardsPool, 5, Color.RED, 1);
        addMultipleCards(cardsPool, 6, Color.RED, 2);

        Collections.shuffle(cardsPool);
        return cardsPool;
    }

    private static LinkedList<Potion> generatePotionsPool() {
        LinkedList<Potion> potionsPool = new LinkedList<>();
        potionsPool.add(new Potion(4, Color.GREEN, 2));
        potionsPool.add(new Potion(6, Color.GREEN, 2));
        potionsPool.add(new Potion(7, Color.GREEN, 2));
        potionsPool.add(new Potion(8, Color.GREEN, 2));
        potionsPool.add(new Potion(9, Color.GREEN, 2));
        potionsPool.add(new Potion(10, Color.GREEN, 3));
        potionsPool.add(new Potion(3, Color.ORANGE, 2));
        potionsPool.add(new Potion(5, Color.ORANGE, 2));
        potionsPool.add(new Potion(6, Color.ORANGE, 2));
        potionsPool.add(new Potion(8, Color.ORANGE, 3));
        potionsPool.add(new Potion(9, Color.ORANGE, 3));
        potionsPool.add(new Potion(10, Color.ORANGE, 3));
        potionsPool.add(new Potion(4, Color.VIOLET, 2));
        potionsPool.add(new Potion(5, Color.VIOLET, 2));
        potionsPool.add(new Potion(7, Color.VIOLET, 3));
        potionsPool.add(new Potion(8, Color.VIOLET, 2));
        potionsPool.add(new Potion(9, Color.VIOLET, 3));
        potionsPool.add(new Potion(10, Color.VIOLET, 2));
        Collections.shuffle(potionsPool);
        return potionsPool;
    }
}
