package ru.hse.chislius_server.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class RoomCodeResponse {
    private final String code;
}
