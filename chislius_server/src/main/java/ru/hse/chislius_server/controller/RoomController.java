package ru.hse.chislius_server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.chislius_server.dto.room.CreatePrivateRoomRequest;
import ru.hse.chislius_server.dto.room.RoomCodeResponse;
import ru.hse.chislius_server.dto.room.RoomResponse;
import ru.hse.chislius_server.mapper.RoomMapper;
import ru.hse.chislius_server.model.User;
import ru.hse.chislius_server.model.room.Room;
import ru.hse.chislius_server.service.RoomService;
import ru.hse.chislius_server.service.UserService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    private final UserService userService;
    private final RoomService roomService;
    private final RoomMapper roomMapper;

    @PostMapping
    public RoomCodeResponse createPrivateRoom(@RequestBody CreatePrivateRoomRequest request) {
        User user = userService.getCurrentUser();
        roomService.validateUserNotInRoom(user);
        Room room = roomService.createPrivateRoom(user, request.capacity());
        roomService.broadcastRoom(room);
        return new RoomCodeResponse(room.getCode());
    }

    @PostMapping("/{code}/join")
    public RoomCodeResponse joinPrivateRoom(@PathVariable String code) {
        User user = userService.getCurrentUser();
        roomService.validateUserNotInRoom(user);
        Room room = roomService.joinPrivateRoom(user, code);
        roomService.broadcastRoom(room);
        return new RoomCodeResponse(room.getCode());
    }

    @PostMapping("/public/join")
    public RoomCodeResponse joinPublicRoom() {
        User user = userService.getCurrentUser();
        roomService.validateUserNotInRoom(user);
        Room room = roomService.joinPublicRoom(user);
        roomService.broadcastRoom(room);
        return new RoomCodeResponse(room.getCode());
    }

    @GetMapping("/current")
    public RoomResponse getCurrentRoom() {
        User user = userService.getCurrentUser();
        Room room = roomService.getCurrentRoom(user);
        return roomMapper.toRoomResponse(room);
    }

    @PostMapping("/current/ready")
    public void leaveCurrentRoom() {
        User user = userService.getCurrentUser();
        Room room = roomService.getCurrentRoom(user);
        roomService.leaveRoom(user, room);
        roomService.broadcastRoom(room);
    }
}
