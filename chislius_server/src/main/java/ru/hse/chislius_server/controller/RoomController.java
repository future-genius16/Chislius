package ru.hse.chislius_server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.chislius_server.dto.room.CreatePrivateRoomRequest;
import ru.hse.chislius_server.dto.room.RoomCodeResponse;
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

    @PostMapping
    public RoomCodeResponse createPrivateRoom(@RequestBody CreatePrivateRoomRequest request) {
        User user = userService.getCurrentUser();
        roomService.validateUserNotInRoom(user);
        Room room = roomService.createPrivateRoom(user, request.capacity(), request.mode());
        roomService.broadcastRoom(room);
        return new RoomCodeResponse(room.getCode());
    }

    @PostMapping("/join/{code}")
    public RoomCodeResponse joinPrivateRoom(@PathVariable String code) {
        User user = userService.getCurrentUser();
        roomService.validateUserNotInRoom(user);
        Room room = roomService.joinPrivateRoom(user, code);
        roomService.broadcastRoom(room);
        return new RoomCodeResponse(room.getCode());
    }

    @PostMapping("/join/public")
    public RoomCodeResponse joinPublicRoom() {
        User user = userService.getCurrentUser();
        roomService.validateUserNotInRoom(user);
        Room room = roomService.joinPublicRoom(user);
        roomService.broadcastRoom(room);
        return new RoomCodeResponse(room.getCode());
    }

    @PostMapping("/leave")
    public void leaveCurrentRoom() {
        User user = userService.getCurrentUser();
        Room room = roomService.getCurrentRoom(user);
        roomService.leaveRoom(user, room);
        roomService.broadcastRoom(room);
    }

    @PostMapping("/flip/{id}")
    public void flipCard(@PathVariable int id) {
        User user = userService.getCurrentUser();
        Room room = roomService.getCurrentRoom(user);
        roomService.flipCard(user, room, id);
        roomService.broadcastRoom(room);
    }

    @PostMapping("/skip")
    public void skipMove() {
        User user = userService.getCurrentUser();
        Room room = roomService.getCurrentRoom(user);
        roomService.skipMove(user, room);
        roomService.broadcastRoom(room);
    }

    @PostMapping("/submit")
    public void submitMove() {
        User user = userService.getCurrentUser();
        Room room = roomService.getCurrentRoom(user);
        roomService.submitMove(user, room);
        roomService.broadcastRoom(room);
    }
}
