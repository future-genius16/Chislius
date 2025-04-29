package ru.hse.chislius_server.game;

public enum Color {
    BLUE {
        @Override
        public Color combine(Color color) {
            return switch (color) {
                case BLUE -> BLUE;
                case YELLOW, GREEN -> GREEN;
                case RED, PURPLE -> PURPLE;
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
                case BLUE, PURPLE -> PURPLE;
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

    PURPLE {
        @Override
        public Color combine(Color color) {
            return switch (color) {
                case PURPLE, BLUE, RED -> PURPLE;
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
