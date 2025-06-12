package ru.hse.chislius_server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.chislius_server.dto.user.UserAuthRequest;
import ru.hse.chislius_server.dto.user.UserChangeAvatarRequest;
import ru.hse.chislius_server.dto.user.UserChangeNameRequest;
import ru.hse.chislius_server.dto.user.UserTokenResponse;
import ru.hse.chislius_server.model.User;
import ru.hse.chislius_server.service.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public UserTokenResponse registerUser(@RequestBody UserAuthRequest request) {
        User user = userService.registerUser(request.getUsername(), request.getPassword());
        return new UserTokenResponse(user.getToken());
    }

    @PostMapping("/login")
    public UserTokenResponse loginUser(@RequestBody UserAuthRequest request) {
        User user = userService.loginUser(request.getUsername(), request.getPassword());
        return new UserTokenResponse(user.getToken());
    }

    @PostMapping("/delete")
    public void deleteUser() {
        userService.deleteUser();
    }

    @GetMapping("/update")
    public void update() {
        userService.getUpdateResponse();
    }

    @PostMapping("/username")
    public void changeUsername(@RequestBody UserChangeNameRequest request) {
        userService.changeUsername(request.getUsername());
    }

    @PostMapping("/avatar")
    public void changeAvatar(@RequestBody UserChangeAvatarRequest request) {
        userService.changeAvatar(request.getAvatar());
    }
}
