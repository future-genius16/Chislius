package ru.hse.chislius_server.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.hse.chislius_server.user.dto.UserLoginRequest;
import ru.hse.chislius_server.user.dto.UserRegisterRequest;
import ru.hse.chislius_server.user.dto.UserResponse;
import ru.hse.chislius_server.user.dto.UserTokenResponse;
import ru.hse.chislius_server.user.service.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public UserTokenResponse register(UserRegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/login")
    public UserTokenResponse login(UserLoginRequest request) {
        return userService.login(request);
    }

    @GetMapping("/{username}")
    public UserResponse get(@PathVariable String username) {
        return userService.getByUsername(username);
    }
}
