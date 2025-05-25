package ru.hse.chislius_server.dto.update;

import lombok.Data;
import ru.hse.chislius_server.game.entity.Potion;

@Data
public class PotionResponse {
    private final int value;
    private final int color;
    private final int quantity;

    public PotionResponse(Potion potion) {
        if (potion == null) {
            value = 0;
            color = 0;
            quantity = 0;
        } else {
            value = potion.getValue();
            color = potion.getColor().ordinal();
            quantity = potion.getQuantity();
        }
    }
}
