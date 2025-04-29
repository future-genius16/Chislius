package ru.hse.chislius_server.game;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Potion {
    private final Color color;
    private final int value;
    private final int quantity;

    public Potion(int color, int value, int quantity) {
        this.color = switch (color - 1) {
            case 0 -> Color.BLUE;
            case 1 -> Color.RED;
            case 2 -> Color.YELLOW;
            default -> Color.UNKNOWN;
        };
        this.value = value;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "[" + color + " " + value + " " + quantity + ']';
    }
}
