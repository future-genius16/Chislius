package ru.hse.chislius_server.repository;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class SessionRepository {
    private final Map<Long, String> userIdToSessionId = new HashMap<>();
    private final Map<String, Long> sessionIdToUserId = new HashMap<>();

    public void addSession(long userId, String sessionId) {
        userIdToSessionId.put(userId, sessionId);
        sessionIdToUserId.put(sessionId, userId);
    }

    public Optional<Long> findUserId(String sessionId) {
        return Optional.ofNullable(sessionIdToUserId.get(sessionId));
    }

    public Optional<String> findSessionId(long userId) {
        return Optional.ofNullable(userIdToSessionId.get(userId));
    }
}
