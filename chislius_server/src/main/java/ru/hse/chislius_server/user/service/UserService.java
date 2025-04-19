package ru.hse.chislius_server.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.hse.chislius_server.config.context.UserContext;
import ru.hse.chislius_server.user.User;
import ru.hse.chislius_server.user.exception.UserNotFoundException;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserService {
    private final UserContext userContext;

    private final Map<Long, User> userMap = new HashMap<>();

    public UserService(UserContext userContext) {
        this.userContext = userContext;
        userMap.put(1L,  new User("Bob"));
    }

    public User getCurrentUser() {
        Long userId = userContext.getUserId();
        if (userId == null) {
            throw new UserNotFoundException("User required");
        }
        return getUser(userId);
    }

    public User getUser(long userId) {
        if (!userMap.containsKey(userId)) {
            throw new UserNotFoundException("User not found");
        }
        return userMap.get(userId);
    }
}