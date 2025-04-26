package ru.hse.chislius_server.game;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Card {
    private final int color;
    final int value;
    private boolean open;

    public Card(int color, int value) {
        this.color = color;
        this.value = value;
        this.open = false;
    }

    @Override
    public String toString() {
        if (open) {
            return "[" + color + ", " + value + "]";
        } else {
            return "[0, 0]";
        }
    }
}
