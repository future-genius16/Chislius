package ru.hse.chislius_server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import ru.hse.chislius_server.exception.DataValidationException;
import ru.hse.chislius_server.repository.SessionRepository;
import ru.hse.chislius_server.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketService {
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final RoomService roomService;

    @EventListener
    public void handleSubscription(SessionSubscribeEvent event) {
        StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headers.getSessionId();
        String destination = headers.getDestination();

        if (destination != null && destination.startsWith("/user/") && destination.endsWith("/update")) {
            String[] parts = destination.split("/");
            String token = parts[2];
            userRepository.findByToken(token).ifPresent(user -> {
                sessionRepository.addSession(user.getId(), sessionId);
                log.info("Connected {}", user);
            });
        }
    }

    @EventListener
    public void onDisconnectEvent(SessionDisconnectEvent event) {
        final String sessionId = event.getSessionId();

        sessionRepository.findUserId(sessionId).flatMap(userRepository::findById).ifPresent((user) -> new Thread(() -> {
            log.info("Start delete waiting for {}", user);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            sessionRepository.findSessionId(user.getId()).ifPresent(currentSessionId -> {
                if (currentSessionId.equals(sessionId)) {
                    try {
                        roomService.leaveRoom(user.getId());
                    } catch (DataValidationException ignored) {
                    }
                    sessionRepository.deleteSession(user.getId(), sessionId);
                    log.info("End delete waiting for {}", user);
                } else {
                    log.info("{} reconnected", user);
                }
            });
        }).start());
    }
}
