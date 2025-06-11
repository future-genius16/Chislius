package ru.hse.chislius_server.dto.update;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public final class RoomUpdate {
    private final String code;
    private final boolean open;
    private final int capacity;
    private final int mode;
    private final List<UserResponse> players;
    private final UserResponse currentPlayer;
    private final GameResponse board;

}