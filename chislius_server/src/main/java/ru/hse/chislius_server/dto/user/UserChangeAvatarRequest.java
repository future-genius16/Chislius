package ru.hse.chislius_server.dto.user;

import lombok.Data;

@Data
public class UserChangeAvatarRequest {
    private final int avatar;
}