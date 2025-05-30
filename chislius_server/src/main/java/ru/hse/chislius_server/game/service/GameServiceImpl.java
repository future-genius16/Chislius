package ru.hse.chislius_server.game.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;

    private static final int BOARD_SIZE = 4;
    private static final int POTIONS_SIZE = 3;
    private static final int MAX_OPEN_CARD = 3;

    @Value("${config.game.wrong-move-cost}")
    private int WRONG_MOVE_COST;

    public Game createGame(String key, GameMode gameMode) {
        log.debug("Try to create game {} with mode {}", key, gameMode);
        if (key == null || key.isEmpty() || gameRepository.get(key) != null) {
            log.warn("Can't create game. Key {} invalid or game already exists", key);
            return null;
        }

        Game game = new Game(key, BOARD_SIZE, gameMode, generateCardsPool(), generatePotionsPool());
        log.info("Game {}: Create", key);

        for (int i = 0; i < game.size * game.size; i++) {
            game.cards.add(game.cardsPool.pop());
        }
        for (int i = 0; i < POTIONS_SIZE; i++) {
            game.potions.add(game.potionsPool.pop());
        }

        gameRepository.add(key, game);
        log.info("Game {}: Initialization complete", key);
        sendChangesToPlayers(game);
        return game;
    }

    public Game getGame(String key) {
        log.debug("Get game {} from repository", key);
        return gameRepository.get(key);
    }

    public boolean openCard(Game game, int i) {
        log.debug("Game {}: Try to open card {}", game.key, i);
        if (game.openCards.size() < MAX_OPEN_CARD) {
            Card card = game.cards.get(i);
            if (card != null && !card.isOpen()) {
                card.setOpen(true);
                game.openCards.add(card);
                log.debug("Game {}: Open card {}", game.key, i);
                sendChangesToPlayers(game);
                return true;
            }
        }
        log.debug("Game {}: Can't open card {}", game.key, i);

        return false;
    }

    public int doMove(Game game) {
        log.debug("Game {}: Try to do move", game.key);
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
                        log.debug("Game {}: Successful move. Return {}", game.key, potion.getValue());
                        dropOpenCards(game);
                        dropPotion(game, potion);
                        sendChangesToPlayers(game);
                        return potion.getValue();
                    }
                }
            }
        }

        log.debug("Game {}: Wrond move. Return {}", game.key, WRONG_MOVE_COST);
        flipOpenCards(game);
        sendChangesToPlayers(game);
        return WRONG_MOVE_COST;
    }

    public void skipMove(Game game) {
        log.debug("Game {}: Skip move", game.key);
        flipOpenCards(game);
        sendChangesToPlayers(game);
    }

    public boolean canMove(Game game) {
        log.debug("Game {}: Check can move", game.key);
        for (Potion potion : game.potions) {
            for (int i1 = 0; i1 < game.cards.size(); i1++) {
                Card card1 = game.cards.get(i1);
                if (card1 != null && card1.getValue() < potion.getValue() && checkColorNotUnknown(game, card1.getColor().combine(potion.getColor()))) {
                    for (int i2 = i1 + 1; i2 < game.cards.size(); i2++) {
                        Card card2 = game.cards.get(i2);
                        if (card2 != null) {
                            if (game.gameMode.equals(GameMode.EASY) || potion.getQuantity() == 2) {
                                if (card1.getValue() + card2.getValue() == potion.getValue() && checkColorEquals(game, card1.getColor().combine(card2.getColor()), potion.getColor())) {
                                    log.debug("Game {}: Can create {} with {}, {}", game.key, potion, card1, card2);
                                    return true;
                                }
                            }
                            if (game.gameMode.equals(GameMode.EASY) || potion.getQuantity() == 3) {
                                for (int i3 = i2 + 1; i3 < game.cards.size(); i3++) {
                                    Card card3 = game.cards.get(i3);
                                    if (card3 != null && card1.getValue() + card2.getValue() + card3.getValue() == potion.getValue() && checkColorEquals(game, card1.getColor().combine(card2.getColor()).combine(card3.getColor()), potion.getColor())) {
                                        log.debug("Game {}: Can create {} with {}, {}, {}", game.key, potion, card1, card2, card3);
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        log.debug("Game {}: Can't find valid moves", game.key);
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
        log.debug("Game {}: Flip open cards", game.key);
    }

    private void dropOpenCards(Game game) {
        log.debug("Game {}: Drop open cards", game.key);
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
        log.debug("Game {}: Drop potion {}", game.key, potion);
        if (!game.potionsPool.isEmpty()) {
            game.potions.add(game.potionsPool.pop());
        }
    }

    private void dropCard(Game game, int i) {
        log.debug("Game {}: Drop card {}", game.key, i);
        game.cards.remove(i);
        if (!game.cardsPool.isEmpty()) {
            game.cards.add(i, game.cardsPool.pop());
            log.trace("Game {}: Add new card from pool to position {}", game.key, i);
        } else {
            game.cards.add(i, null);
            log.trace("Game {}: Add null to position {}", game.key, i);
        }
    }

    private static void addMultipleCards(LinkedList<Card> cards, int value, Color color, int quantity) {
        for (int i = 0; i < quantity; i++) {
            cards.add(new Card(value, color));
        }
    }

    private static LinkedList<Card> generateCardsPool() {
        log.debug("Generate cards pool: Start");
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
        log.debug("Generate cards pool: Complete");
        return cardsPool;
    }

    private static LinkedList<Potion> generatePotionsPool() {
        log.debug("Generate potions pool: Start");
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
        log.debug("Generate potions pool: Complete");
        return potionsPool;
    }
}