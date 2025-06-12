package ru.hse.chislius_server.dto.update;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserMessageResponse {
    private final long id;
    private final String name;
    private final int avatar;
}
