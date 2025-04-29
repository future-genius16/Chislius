package ru.hse.chislius_server.game;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Card {
    private final Color color;
    final int value;
    private boolean open;

    public Card(int color, int value) {
        this.color = switch (color - 1) {
            case 0 -> Color.BLUE;
            case 1 -> Color.RED;
            case 2 -> Color.YELLOW;
            default -> Color.UNKNOWN;
        };
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
