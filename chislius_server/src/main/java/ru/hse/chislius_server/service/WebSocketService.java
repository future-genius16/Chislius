package ru.hse.chislius_server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import ru.hse.chislius_server.model.User;
import ru.hse.chislius_server.model.room.Room;
import ru.hse.chislius_server.repository.SessionRepository;
import ru.hse.chislius_server.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
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
            String[] parts = destination.split("/"); // Разделяем по "/"
            String token = parts[2];
            userRepository.findByToken(token).ifPresent(user -> {
                sessionRepository.addSession(user.getId(), sessionId);
                System.out.println(sessionId);
                System.out.println(user);
                System.out.println("Connect");
            });
        }
    }

    @EventListener
    public void onDisconnectEvent(SessionDisconnectEvent event) {
        final String sessionId = event.getSessionId();

        Optional<Long> optional = sessionRepository.findUserId(sessionId);
        if (optional.isPresent()) {
            final User user = userRepository.findById(optional.get()).orElse(null);

            if (user != null) {
                Room room = roomService.getCurrentRoom(user.getId());
                if (room != null) {
                    new Thread(() -> {
                        System.out.println("Start delete waiting");
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        sessionRepository.findSessionId(user.getId()).ifPresent(currentSessionId -> {
                            if (currentSessionId.equals(sessionId)) {
                                roomService.leaveRoom(user.getId());
                                System.out.println("End delete waiting");
                            } else {
                                System.out.println("User reconnected");
                            }
                        });
                    }).start();
                }
            }
        }
    }
}
