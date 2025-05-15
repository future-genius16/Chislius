package ru.hse.chislius_server.dto.room;

import ru.hse.chislius_server.model.room.RoomState;
import ru.hse.chislius_server.model.room.RoomType;

import java.util.List;

public record RoomResponse(String code, RoomType type, int capacity, RoomState state, List<String> usernames) {
}