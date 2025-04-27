package ru.hse.chislius_server.user.dto;

import lombok.Data;

@Data
public class UserRegisterRequest {
    private final String username;
    private final String password;
}