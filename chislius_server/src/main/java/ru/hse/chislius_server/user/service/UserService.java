package ru.hse.chislius_server.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.hse.chislius_server.config.context.UserContext;
import ru.hse.chislius_server.user.User;
import ru.hse.chislius_server.user.dto.UserLoginRequest;
import ru.hse.chislius_server.user.dto.UserRegisterRequest;
import ru.hse.chislius_server.user.dto.UserResponse;
import ru.hse.chislius_server.user.dto.UserTokenResponse;
import ru.hse.chislius_server.user.exception.UnableRegisterUserException;
import ru.hse.chislius_server.user.exception.UserNotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {
    private final UserContext userContext;

    private final Map<String, User> userMap = new HashMap<>();
    private final Map<String, User> tokenMap = new HashMap<>();

    private final Random random = new Random();

    public User getCurrentUser() {
        String token = userContext.getUserToken();
        if (token == null) {
            throw new UserNotFoundException("User required");
        }
        return getByToken(token);
    }

    public UserTokenResponse register(UserRegisterRequest request) {
        User user = new User(request.getUsername(), request.getPassword());
        addUserToUserMap(user);
        String token = addUserToTokenMap(user);
        return new UserTokenResponse(token);
    }

    public UserTokenResponse login(UserLoginRequest request) {
        User user = getUserByUsername(request.getUsername());
        if (!user.getPassword().equals(request.getPassword())) {
            throw new UnableRegisterUserException("Incorrect password");
        }
        String token = addUserToTokenMap(user);
        return new UserTokenResponse(token);
    }

    public UserResponse getByUsername(String username) {
        User user = getUserByUsername(username);
        return new UserResponse(user);
    }

    private void addUserToUserMap(User user) {
        synchronized (userMap) {
            if (userMap.containsKey(user.getUsername())) {
                throw new IllegalStateException("User already exists");
            }
            userMap.put(user.getUsername(), user);
        }
    }

    private String addUserToTokenMap(User user) {
        int counter = 0;
        while (counter < 10) {
            String token = generateToken();
            synchronized (tokenMap) {
                if (!tokenMap.containsKey(token)) {
                    tokenMap.put(token, user);
                    return token;
                } else {
                    counter++;
                }
            }
        }
        throw new UnableRegisterUserException("Please try again");
    }

    private String generateToken() {
        int number = random.nextInt(100000);
        return String.format("%06d", number);
    }

    private User getByToken(String token) {
        if (!tokenMap.containsKey(token)) {
            throw new UserNotFoundException("User not found");
        }
        return tokenMap.get(token);
    }

    private User getUserByUsername(String username) {
        if (!userMap.containsKey(username)) {
            throw new UserNotFoundException("User not found");
        }
        return userMap.get(username);
    }
}