package ru.hse.chislius_server.game;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Card {
    private final int color;
    private final int value;
    private boolean visible;

    public Card(int color, int value) {
        this.color = color;
        this.value = value;
        this.visible = false;
    }

    @Override
    public String toString() {
        if (visible) {
            return "[" + color + ", " + value + "]";
        } else {
            return "[0, 0]";
        }
    }
}
