package ru.hse.chislius_server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.hse.chislius_server.config.context.UserContext;
import ru.hse.chislius_server.dto.update.UpdateResponse;
import ru.hse.chislius_server.exception.AuthorizationException;
import ru.hse.chislius_server.exception.DataValidationException;
import ru.hse.chislius_server.exception.EntityNotFoundException;
import ru.hse.chislius_server.exception.GenerationTimeoutException;
import ru.hse.chislius_server.model.User;
import ru.hse.chislius_server.repository.UserRepository;

import java.util.Random;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {
    private final UserContext userContext;
    private final UserRepository userRepository;
    private final UpdateService updateService;

    public User getCurrentUser() {
        String token = userContext.getUserToken();
        if (token == null) {
            throw new AuthorizationException("Необходимо войти для совершения этого действия");
        }
        return getUserByToken(token);
    }

    public User registerUser(String username, String password) {
        User user = new User(username);
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        user.setPassword(hashedPassword);
        save(user);
        return user;
    }

    public User loginUser(String username, String password) {
        User user = getUserByUsername(username);
        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new DataValidationException("Введен неверный пароль");
        }
        issueToken(user);
        return user;
    }

    public void deleteUser() {
        User user = getCurrentUser();
        userRepository.delete(user);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    private void save(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new DataValidationException("Данное имя пользователя уже занято");
        }

        userRepository.save(user);
        issueToken(user);
    }

    private void issueToken(User user) {
        int counter = 0;
        while (counter < 10) {
            String token = generateToken();
            if (!userRepository.existsByToken(token)) {
                user.setToken(token);
                userRepository.save(user);
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
        return userRepository.findByToken(token).orElseThrow(() -> new AuthorizationException("Пользователь не найден"));
    }

    public void getUpdateResponse() {
        updateService.sendUpdate(getCurrentUser().getId());
    }

    public void changeUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new DataValidationException("Задано пустое имя пользователя");
        }
        User user = getCurrentUser();
        user.setUsername(username);
        userRepository.save(user);
        updateService.sendUpdate(user.getId());
    }

    public void changeAvatar(int avatar) {
        if (avatar < 0 || avatar > 11) {
            throw new DataValidationException("Некорректный запрос");
        }
        User user = getCurrentUser();
        user.setAvatar(avatar);
        userRepository.save(user);
        updateService.sendUpdate(user.getId());
    }
}