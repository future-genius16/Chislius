package ru.hse.chislius_server.dto.update;

import lombok.Data;
import ru.hse.chislius_server.game.entity.Game;

import java.util.List;

@Data
public class GameResponse {
    private final List<PotionResponse> potions;
    private final List<Integer> cards;

    public GameResponse(Game game) {
        potions = game.potions.stream().map((potion) -> new PotionResponse(potion, game.gameMode)).toList();
        cards = game.cards.stream().map((card -> {
            if (card == null) return null;
            if (!card.isOpen()) {
                return 0;
            } else {
                return card.getColor().ordinal() * 6 + card.getValue();
            }
        })).toList();
    }
}
