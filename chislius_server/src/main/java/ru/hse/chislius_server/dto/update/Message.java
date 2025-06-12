package ru.hse.chislius_server.dto.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Message {
    private final MessageType type;
    private final UserMessageResponse user;
    private int score;
}
