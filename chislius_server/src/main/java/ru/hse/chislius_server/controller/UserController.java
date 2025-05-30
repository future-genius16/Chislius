package ru.hse.chislius_server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.hse.chislius_server.dto.update.UpdateResponse;
import ru.hse.chislius_server.dto.user.UserChangeNameRequest;
import ru.hse.chislius_server.dto.user.UserLoginRequest;
import ru.hse.chislius_server.dto.user.UserRegisterRequest;
import ru.hse.chislius_server.dto.user.UserTokenResponse;
import ru.hse.chislius_server.model.User;
import ru.hse.chislius_server.service.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public UserTokenResponse registerUser(@RequestBody UserRegisterRequest request) {
        User user = userService.registerUser(request.getUsername(), request.getPassword());
        return new UserTokenResponse(user.getToken());
    }

    @PostMapping("/login")
    public UserTokenResponse loginUser(@RequestBody UserLoginRequest request) {
        User user = userService.loginUser(request.getUsername(), request.getPassword());
        return new UserTokenResponse(user.getToken());
    }

    @GetMapping("/update")
    public void update(){
        userService.getUpdateResponse();
    }

    @PostMapping("/username")
    public void changeUsername(@RequestBody UserChangeNameRequest request) {
        userService.changeUsername(request.getUsername());
    }
}
