package ru.hse.chislius_server.room.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.chislius_server.room.dto.*;
import ru.hse.chislius_server.room.service.RoomService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomService roomService;

    @PostMapping("/ping")
    public void ping() {
        roomService.ping();
    }

    @PostMapping
    public RoomCodeResponse createPrivate(CreatePrivateRoomRequest request) {
        return roomService.createPrivateRoom(request);
    }

    @PostMapping("/{roomId}/connect")
    public RoomCodeResponse connectPrivate(@PathVariable String roomId) {
        return roomService.joinPrivateRoom(roomId);
    }

    @PostMapping("/connect")
    public RoomCodeResponse connectPublic() {
        return roomService.joinPublicRoom();
    }

    @GetMapping("/{roomId}")
    public RoomResponse get(@PathVariable String roomId) {
        return roomService.getRoom(roomId);
    }

    @DeleteMapping("/{roomId}")
    public void delete(@PathVariable String roomId) {
        roomService.deleteRoom(roomId);
    }
}
