package ru.hse.chislius_server.dto.update;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserResponse {
    private final long id;
    private final String name;
    private final int rating;
    private final int score;
}
