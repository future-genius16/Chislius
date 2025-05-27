package ru.hse.chislius_server.dto.user;

import lombok.Data;

@Data
public class UserChangeNameRequest {
    private final String username;
}