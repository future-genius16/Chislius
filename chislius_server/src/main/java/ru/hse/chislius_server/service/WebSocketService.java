package ru.hse.chislius_server.service;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import ru.hse.chislius_server.exception.AuthorizationException;
import ru.hse.chislius_server.model.User;
import ru.hse.chislius_server.model.room.Room;

@Component
public class WebSocketService {
    private final UserService userService;
    private final RoomService roomService;

    public WebSocketService(UserService userService, RoomService roomService) {
        this.userService = userService;
        this.roomService = roomService;
    }

    @EventListener
    public void handleSubscription(SessionSubscribeEvent event) {
        StompHeaderAccessor headers = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headers.getSessionId();
        String destination = headers.getDestination();

        if (destination != null && destination.startsWith("/user/") && destination.endsWith("/update")) {
            String[] parts = destination.split("/"); // Разделяем по "/"
            String token = parts[2];
            try {
                User user = userService.getUserByToken(token);
                user.setSessionId(sessionId);
                System.out.println(sessionId);
                System.out.println(user);
                System.out.println("Connect");
            } catch (AuthorizationException ignored) {
            }
        }
    }

    @EventListener
    public void onDisconnectEvent(SessionDisconnectEvent event) {
        final User user = userService.getUserBySessionId(event.getSessionId());
        final String sessionId = event.getSessionId();
        if (user != null) {
            Room room = user.getCurrentRoom();
            if (room != null) {
                new Thread(() -> {
                    System.out.println("Start delete waiting");
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    if (user.getSessionId().equals(sessionId)) {
                        roomService.leaveRoom(user, room);
                        System.out.println("End delete waiting");
                    } else {
                        System.out.println("User reconnected");
                    }
                }).start();
            }
        }
    }
}
