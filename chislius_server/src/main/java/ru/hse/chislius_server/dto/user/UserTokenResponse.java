package ru.hse.chislius_server.dto.user;

import lombok.Data;

@Data
public class UserTokenResponse {
    private final String token;
}