package ru.hse.chislius_server.room.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.hse.chislius_server.room.dto.*;
import ru.hse.chislius_server.room.service.RoomService;
import ru.hse.chislius_server.room.exception.UnableCreateRoomException;
import ru.hse.chislius_server.user.User;

@Slf4j
@RequiredArgsConstructor
@Controller
public class RoomController {
    private final SimpMessagingTemplate messagingTemplate;
    private final RoomService roomService;

    @MessageMapping("/connect")
    @SendTo("/topic/rooms-updates")
    public RoomsUpdateResponse connect() {
        return new RoomsUpdateResponse(roomService.getRoomMap().values().stream().toList());
    }

    @MessageMapping("/connect-public")
    @SendTo("/topic/rooms-updates")
    public RoomsUpdateResponse connectPublic(ConnectPublicRoomRequest request) {
        log.info("Received connect-public request: " + request);
        User user = roomService.getUser(request.getUsername());
        try {
            String connectedCode = roomService.joinPublicRoom(user);
            RoomCodeResponse response = new RoomCodeResponse(connectedCode);
            messagingTemplate.convertAndSendToUser(user.getUsername(), "/queue/code", response);
            log.info("Send queue/code response: " + response);
        } catch (UnableCreateRoomException e) {
            messagingTemplate.convertAndSendToUser(user.getUsername(), "/topic/errors", e.getClass().getName());
        }
        return new RoomsUpdateResponse(roomService.getRoomMap().values().stream().toList());
    }

    @MessageMapping("/create-private")
    @SendTo("/topic/rooms-updates")
    public RoomsUpdateResponse createPrivate(CreatePrivateRoomRequest request) {
        log.info("Received create-private request: " + request);
        User user = roomService.getUser(request.getUsername());
        try {
            String connectedCode = roomService.createPrivateRoom(user);
            RoomCodeResponse response = new RoomCodeResponse(connectedCode);
            messagingTemplate.convertAndSendToUser(user.getUsername(), "/queue/code", response);
            log.info("Send queue/code response: " + response);
        } catch (UnableCreateRoomException e) {
            messagingTemplate.convertAndSendToUser(user.getUsername(), "/errors", e.getClass().getName());
        }
        return new RoomsUpdateResponse(roomService.getRoomMap().values().stream().toList());
    }

    @MessageMapping("/connect-private")
    @SendTo("/topic/rooms-updates")
    public RoomsUpdateResponse connectPrivate(ConnectPrivateRoomRequest request) {
        log.info("Received connect-private request: " + request);
        User user = roomService.getUser(request.getUsername());
        String connectedCode = roomService.joinPrivateRoom(user, request.getCode());
        if (connectedCode != null) {
            RoomCodeResponse response = new RoomCodeResponse(connectedCode);
            messagingTemplate.convertAndSendToUser(user.getUsername(), "/queue/code", response);
            log.info("Send queue/code response: " + response);
        }
        return new RoomsUpdateResponse(roomService.getRoomMap().values().stream().toList());
    }

}
