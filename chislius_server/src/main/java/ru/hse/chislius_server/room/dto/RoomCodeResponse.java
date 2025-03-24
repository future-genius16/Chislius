package ru.hse.chislius_server.room.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class RoomCodeResponse {
    private final String code;
}
