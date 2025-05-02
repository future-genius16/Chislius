package ru.hse.chislius_server.game;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Card {
    final int value;
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
