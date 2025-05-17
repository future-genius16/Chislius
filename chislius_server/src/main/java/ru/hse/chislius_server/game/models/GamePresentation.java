package ru.hse.chislius_server.game.models;

import lombok.Data;
import ru.hse.chislius_server.game.entity.Card;
import ru.hse.chislius_server.game.entity.Potion;

import java.util.List;

public record GamePresentation(GameMode gameMode, List<Potion> potions, List<Card> cards) {
}
