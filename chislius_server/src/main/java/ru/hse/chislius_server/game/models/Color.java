package ru.hse.chislius_server.game.models;

public enum Color {
    BLUE {
        @Override
        public Color combine(Color color) {
            return switch (color) {
                case BLUE -> BLUE;
                case YELLOW, GREEN -> GREEN;
                case RED, VIOLET -> VIOLET;
                default -> UNKNOWN;
            };
        }
    }, YELLOW {
        @Override
        public Color combine(Color color) {
            return switch (color) {
                case YELLOW -> YELLOW;
                case BLUE, GREEN -> GREEN;
                case RED, ORANGE -> ORANGE;
                default -> UNKNOWN;
            };
        }
    }, RED {
        @Override
        public Color combine(Color color) {
            return switch (color) {
                case RED -> RED;
                case BLUE, VIOLET -> VIOLET;
                case YELLOW, ORANGE -> ORANGE;
                default -> UNKNOWN;
            };
        }
    }, GREEN {
        @Override
        public Color combine(Color color) {
            return switch (color) {
                case GREEN, BLUE, YELLOW -> GREEN;
                default -> UNKNOWN;
            };
        }
    },

    VIOLET {
        @Override
        public Color combine(Color color) {
            return switch (color) {
                case VIOLET, BLUE, RED -> VIOLET;
                default -> UNKNOWN;
            };
        }
    },

    ORANGE {
        @Override
        public Color combine(Color color) {
            return switch (color) {
                case ORANGE, YELLOW, RED -> ORANGE;
                default -> UNKNOWN;
            };
        }
    }, UNKNOWN {
        @Override
        public Color combine(Color color) {
            return UNKNOWN;
        }
    };

    public abstract Color combine(Color color);
}
