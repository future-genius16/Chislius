package ru.hse.chislius_server.dto.update;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UpdateResponse {
    private final int state;
    private final UserResponse player;
    private final RoomUpdate data;
}
