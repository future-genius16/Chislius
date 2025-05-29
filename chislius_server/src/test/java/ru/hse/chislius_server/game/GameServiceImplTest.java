package ru.hse.chislius_server.game;

import org.junit.jupiter.api.Test;
import ru.hse.chislius_server.game.entity.Card;
import ru.hse.chislius_server.game.entity.Game;
import ru.hse.chislius_server.game.entity.Potion;
import ru.hse.chislius_server.game.models.Color;
import ru.hse.chislius_server.game.models.GameMode;
import ru.hse.chislius_server.game.models.GamePresentation;
import ru.hse.chislius_server.game.repository.GameRepository;
import ru.hse.chislius_server.game.service.GameService;
import ru.hse.chislius_server.game.service.GameServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameServiceImplTest {
    private final GameService gameService = new GameServiceImpl(new GameRepository());


    @Test
    void createGame() {
        GameMode gameMode = GameMode.EASY;
        Game game = gameService.createGame("Test game", gameMode);

        assertNotNull(game);
        assertEquals(gameMode, game.gameMode);
        assertEquals(16, game.cards.size());
        assertEquals(3, game.potions.size());
        assertFalse(game.cardsPool.isEmpty());
        assertFalse(game.potionsPool.isEmpty());
        assertEquals(42 - 16, game.cardsPool.size());
        assertEquals(18 - 3, game.potionsPool.size());
    }

    @Test
    void openCard_ShouldOpenCardIfValid() {
        Game game = gameService.createGame("Test game", GameMode.EASY);
        assertTrue(gameService.openCard(game, 0));
        assertTrue(game.cards.get(0).isOpen());
    }

    @Test
    void openCard_ShouldNotOpenAlreadyOpenedCard() {
        Game game = gameService.createGame("Test game", GameMode.EASY);
        gameService.openCard(game, 0);
        assertFalse(gameService.openCard(game, 0));
    }

    @Test
    void openCard_ShouldNotExceedMaxOpenCards() {
        Game game = gameService.createGame("Test game", GameMode.EASY);
        for (int i = 0; i < 3; i++) {
            gameService.openCard(game, i);
        }
        assertFalse(gameService.openCard(game, 3));
    }

    @Test
    void doMove_GameModeEasy_Success() {
        Game game = gameService.createGame("Test game", GameMode.EASY);

        Potion potion = new Potion(5, Color.VIOLET, 3);
        game.potions.clear();
        game.potions.add(potion);

        game.cards.clear();
        game.cards.add(new Card(2, Color.BLUE));
        game.cards.add(new Card(3, Color.GREEN));

        gameService.openCard(game, 0);
        gameService.openCard(game, 1);

        assertEquals(5, gameService.doMove(game));
        assertTrue(game.openCards.isEmpty());
        assertFalse(game.potions.contains(potion));
    }

    @Test
    void doMove_GameModeEasy_Fail() {
        Game game = gameService.createGame("Test game", GameMode.EASY);

        Potion potion = new Potion(6, Color.VIOLET, 3);
        game.potions.clear();
        game.potions.add(potion);

        game.cards.clear();
        game.cards.add(new Card(2, Color.BLUE));
        game.cards.add(new Card(3, Color.GREEN));

        gameService.openCard(game, 0);
        gameService.openCard(game, 1);

        assertEquals(0, gameService.doMove(game));
        assertTrue(game.openCards.isEmpty());
        assertTrue(game.potions.contains(potion));
    }

    @Test
    void doMove_GameModeMedium_Success() {
        Game game = gameService.createGame("Test game", GameMode.MEDIUM);

        Potion potion = new Potion(5, Color.VIOLET, 2);
        game.potions.clear();
        game.potions.add(potion);

        game.cards.clear();
        game.cards.add(new Card(2, Color.BLUE));
        game.cards.add(new Card(3, Color.GREEN));

        gameService.openCard(game, 0);
        gameService.openCard(game, 1);

        assertEquals(5, gameService.doMove(game));
        assertTrue(game.openCards.isEmpty());
        assertFalse(game.potions.contains(potion));
    }

    @Test
    void doMove_GameModeMedium_Fail() {
        Game game = gameService.createGame("Test game", GameMode.MEDIUM);

        Potion potion = new Potion(6, Color.VIOLET, 3);
        game.potions.clear();
        game.potions.add(potion);

        game.cards.clear();
        game.cards.add(new Card(2, Color.BLUE));
        game.cards.add(new Card(3, Color.GREEN));

        gameService.openCard(game, 0);
        gameService.openCard(game, 1);

        assertEquals(0, gameService.doMove(game));
        assertTrue(game.openCards.isEmpty());
        assertTrue(game.potions.contains(potion));
    }

    @Test
    void doMove_GameModeHard_Success() {
        Game game = gameService.createGame("Test game", GameMode.HARD);

        Potion potion = new Potion(5, Color.VIOLET, 2);
        game.potions.clear();
        game.potions.add(potion);

        game.cards.clear();
        game.cards.add(new Card(2, Color.BLUE));
        game.cards.add(new Card(3, Color.RED));

        gameService.openCard(game, 0);
        gameService.openCard(game, 1);

        assertEquals(5, gameService.doMove(game));
        assertTrue(game.openCards.isEmpty());
        assertFalse(game.potions.contains(potion));
    }

    @Test
    void doMove_GameModeHard_Fail() {
        Game game = gameService.createGame("Test game", GameMode.HARD);

        Potion potion = new Potion(6, Color.VIOLET, 2);
        game.potions.clear();
        game.potions.add(potion);

        game.cards.clear();
        game.cards.add(new Card(2, Color.BLUE));
        game.cards.add(new Card(3, Color.GREEN));

        gameService.openCard(game, 0);
        gameService.openCard(game, 1);

        assertEquals(0, gameService.doMove(game));
        assertTrue(game.openCards.isEmpty());
        assertTrue(game.potions.contains(potion));
    }

    @Test
    void skipMove() {
        Game game = gameService.createGame("Test game", GameMode.EASY);
        gameService.openCard(game, 0);
        gameService.openCard(game, 1);

        gameService.skipMove(game);
        assertTrue(game.openCards.isEmpty());
        assertFalse(game.cards.get(0).isOpen());
    }

    @Test
    void canMove_GameModeEasy_Success() {
        Game game = gameService.createGame("Test game", GameMode.EASY);

        Potion potion = new Potion(5, Color.VIOLET, 3);
        game.potions.clear();
        game.potions.add(potion);

        game.cards.clear();
        game.cards.add(new Card(2, Color.BLUE));
        game.cards.add(new Card(3, Color.GREEN));

        assertTrue(gameService.canMove(game));
    }

    @Test
    void canMove_GameModeEasy_Fail() {
        Game game = gameService.createGame("Test game", GameMode.EASY);

        Potion potion = new Potion(6, Color.VIOLET, 3);
        game.potions.clear();
        game.potions.add(potion);

        game.cards.clear();
        game.cards.add(new Card(2, Color.BLUE));
        game.cards.add(new Card(3, Color.GREEN));

        assertFalse(gameService.canMove(game));
    }

    @Test
    void canMove_GameModeMedium_Success() {
        Game game = gameService.createGame("Test game", GameMode.MEDIUM);

        Potion potion = new Potion(5, Color.VIOLET, 2);
        game.potions.clear();
        game.potions.add(potion);

        game.cards.clear();
        game.cards.add(new Card(2, Color.BLUE));
        game.cards.add(new Card(3, Color.GREEN));

        assertTrue(gameService.canMove(game));
    }

    @Test
    void canMove_GameModeMedium_Fail() {
        Game game = gameService.createGame("Test game", GameMode.MEDIUM);

        Potion potion = new Potion(6, Color.VIOLET, 3);
        game.potions.clear();
        game.potions.add(potion);

        game.cards.clear();
        game.cards.add(new Card(2, Color.BLUE));
        game.cards.add(new Card(3, Color.GREEN));

        assertFalse(gameService.canMove(game));
    }

    @Test
    void canMove_GameModeHard_Success() {
        Game game = gameService.createGame("Test game", GameMode.HARD);

        Potion potion = new Potion(5, Color.VIOLET, 2);
        game.potions.clear();
        game.potions.add(potion);

        game.cards.clear();
        game.cards.add(new Card(2, Color.BLUE));
        game.cards.add(new Card(3, Color.RED));

        assertTrue(gameService.canMove(game));
    }

    @Test
    void canMove_GameModeHard_Fail() {
        Game game = gameService.createGame("Test game", GameMode.HARD);

        Potion potion = new Potion(6, Color.VIOLET, 2);
        game.potions.clear();
        game.potions.add(potion);

        game.cards.clear();
        game.cards.add(new Card(2, Color.BLUE));
        game.cards.add(new Card(3, Color.GREEN));

        assertFalse(gameService.canMove(game));
    }

    @Test
    void getGamePresentation() {
        Game game = gameService.createGame("Test game", GameMode.EASY);
        GamePresentation presentation = gameService.getGamePresentation(game);

        assertEquals(game.gameMode, presentation.gameMode());
        assertEquals(game.cards, presentation.cards());
        assertEquals(game.potions, presentation.potions());
    }
}