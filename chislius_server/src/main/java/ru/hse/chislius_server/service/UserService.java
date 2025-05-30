package ru.hse.chislius_server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.hse.chislius_server.config.context.UserContext;
import ru.hse.chislius_server.dto.update.UpdateResponse;
import ru.hse.chislius_server.exception.AuthorizationException;
import ru.hse.chislius_server.exception.DataValidationException;
import ru.hse.chislius_server.exception.EntityNotFoundException;
import ru.hse.chislius_server.exception.GenerationTimeoutException;
import ru.hse.chislius_server.model.User;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {
    private final UserContext userContext;

    private final Set<User> users = new HashSet<>();
    private final SimpMessagingTemplate simpMessagingTemplate;

    public User getCurrentUser() {
        String token = userContext.getUserToken();
        if (token == null) {
            throw new AuthorizationException("Необходимо войти для совершения этого действия");
        }
        return getUserByToken(token);
    }

    public User registerUser(String username, String password) {
        User user = new User(username);
        user.setPassword(password);
        save(user);
        return user;
    }

    public User loginUser(String username, String password) {
        User user = getUserByUsername(username);
        if (!user.getPassword().equals(password)) {
            throw new DataValidationException("Введен неверный пароль");
        }
        issueToken(user);
        return user;
    }

    public User getUserByUsername(String username) {
        return users.stream().filter((u) -> u.getUsername().equals(username)).findAny().orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
    }

    private void save(User user) {
        synchronized (users) {
            if (users.stream().anyMatch((u) -> u.getUsername().equals(user.getUsername()))) {
                throw new DataValidationException("Данное имя пользователя уже занято");
            }
            issueToken(user);
            users.add(user);
        }
    }

    private void issueToken(User user) {
        int counter = 0;
        while (counter < 10) {
            String token = generateToken();
            if (users.stream().noneMatch((u) -> u.getToken().equals(token))) {
                user.setToken(token);
                return;
            } else {
                counter++;
            }
        }
        throw new GenerationTimeoutException();
    }

    private String generateToken() {
        Random random = new Random();
        int number = random.nextInt(100000);
        return String.format("%06d", number);
    }

    public User getUserByToken(String token) {
        return users.stream().filter((u) -> u.getToken().equals(token)).findAny().orElseThrow(() -> new AuthorizationException("Пользователь не найден"));
    }

    public void getUpdateResponse() {
        sendUpdate(getCurrentUser());
    }

    public void changeUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new DataValidationException("Задано пустое имя пользователя");
        }
        User user = getCurrentUser();
        user.setUsername(username);
    }

    public void sendUpdate(User user) {
        simpMessagingTemplate.convertAndSendToUser(user.getToken(), "/update", new UpdateResponse(user));
    }
}