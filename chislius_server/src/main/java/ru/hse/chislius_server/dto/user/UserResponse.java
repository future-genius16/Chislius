package ru.hse.chislius_server.dto.user;

import lombok.Data;
import ru.hse.chislius_server.model.User;

@Data
public class UserResponse {
    private final String username;

    public UserResponse(User user) {
        username = user.getUsername();
    }
}