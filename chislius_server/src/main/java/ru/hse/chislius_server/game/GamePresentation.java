package ru.hse.chislius_server.game;

import lombok.Data;
import ru.hse.chislius_server.game.models.Card;
import ru.hse.chislius_server.game.models.Potion;

import java.util.List;

@Data
public class GamePresentation {
    final GameMode gameMode;
    final List<Potion> potions;
    final List<Card> cards;
}
