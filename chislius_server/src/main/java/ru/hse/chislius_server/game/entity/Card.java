package ru.hse.chislius_server.game.entity;

import lombok.Getter;
import lombok.Setter;
import ru.hse.chislius_server.game.models.Color;

@Getter
@Setter
public class Card {
    private final int value;
    private final Color color;
    private boolean open;

    public Card(int value, Color color) {
        this.value = value;
        this.color = color;
        this.open = false;
    }

    @Override
    public String toString() {
        if (open) {
            return "[" + value + ", " + color + "]";
        } else {
            return "[" + 0 + ", " + Color.UNKNOWN + "]";
        }
    }
}
