package ru.hse.chislius_server.user.dto;

import lombok.Data;
import ru.hse.chislius_server.user.User;

@Data
public class UserResponse {
    private final String username;

    public UserResponse(User user) {
        username = user.getUsername();
    }
}