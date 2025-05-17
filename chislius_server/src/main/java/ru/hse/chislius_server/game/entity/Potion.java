package ru.hse.chislius_server.game.entity;

import lombok.Getter;
import lombok.Setter;
import ru.hse.chislius_server.game.models.Color;

@Getter
@Setter
public class Potion {
    private final Color color;
    private final int value;
    private final int quantity;

    public Potion(int value, Color color, int quantity) {
        this.value = value;
        this.color = color;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "[" + value + " " + color + " " + quantity + ']';
    }
}
