package ru.hse.chislius_server.user.dto;

import lombok.Data;

@Data
public class UserTokenResponse {
    private final String token;
}