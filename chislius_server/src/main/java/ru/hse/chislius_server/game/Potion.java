package ru.hse.chislius_server.game;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Potion {
    private final int color;
    private final int value;
    private final int quantity;

    public Potion(int color, int value, int quantity) {
        this.color = color;
        this.value = value;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "[" + color + " " + value + " " + quantity + ']';
    }
}
