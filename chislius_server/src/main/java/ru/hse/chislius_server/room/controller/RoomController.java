package ru.hse.chislius_server.room.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.chislius_server.room.dto.*;
import ru.hse.chislius_server.room.exception.UnableConnectRoomException;
import ru.hse.chislius_server.room.service.RoomService;
import ru.hse.chislius_server.room.exception.UnableCreateRoomException;
import ru.hse.chislius_server.user.User;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RoomController {
    private final RoomService roomService;

    @PostMapping("/update")
    public void update() {
        roomService.update();
    }

    @PostMapping("/create/private")
    public RoomCodeResponse createPrivate(CreatePrivateRoomRequest request) throws UnableCreateRoomException {
        log.info("Received /create/private request: {}", request);
        User user = roomService.getUser(request.getUsername());
        String connectedCode = roomService.createPrivateRoom(user);
        RoomCodeResponse response = new RoomCodeResponse(connectedCode);
        log.info("Send /create/private response: {}", response);
        return response;
    }

    @PostMapping("/connect/private")
    public RoomCodeResponse connectPrivate(ConnectPrivateRoomRequest request) throws UnableConnectRoomException {
        log.info("Received /connect/private request: {}", request);
        User user = roomService.getUser(request.getUsername());
        String connectedCode = roomService.joinPrivateRoom(user, request.getCode());
        RoomCodeResponse response = new RoomCodeResponse(connectedCode);
        log.info("Send /connect/private response: {}", response);
        return response;
    }

    @PostMapping("/connect/public")
    public RoomCodeResponse connectPublic(ConnectPublicRoomRequest request) throws UnableCreateRoomException {
        log.info("Received /connect/public request: {}", request);
        User user = roomService.getUser(request.getUsername());
        String connectedCode = roomService.joinPublicRoom(user);
        RoomCodeResponse response = new RoomCodeResponse(connectedCode);
        log.info("Send /connect/public response: {}", response);
        return response;
    }
}
