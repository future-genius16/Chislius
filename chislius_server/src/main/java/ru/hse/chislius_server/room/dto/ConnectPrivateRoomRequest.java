package ru.hse.chislius_server.room.dto;

import lombok.Data;

@Data
public class ConnectPrivateRoomRequest {
    private final String username;
    private final String code;
}
