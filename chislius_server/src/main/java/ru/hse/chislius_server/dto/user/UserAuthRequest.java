package ru.hse.chislius_server.dto.user;

import lombok.Data;

@Data
public class UserAuthRequest {
    private final String username;
    private final String password;
}