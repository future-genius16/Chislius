package ru.hse.chislius_server.room.dto;

import lombok.Data;

@Data
public class CreatePrivateRoomRequest {
    private final String name;
}
