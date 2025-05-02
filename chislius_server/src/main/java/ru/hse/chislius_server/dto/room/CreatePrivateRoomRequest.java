package ru.hse.chislius_server.dto.room;

import lombok.Data;

@Data
public class CreatePrivateRoomRequest {
    private final String name;
}
