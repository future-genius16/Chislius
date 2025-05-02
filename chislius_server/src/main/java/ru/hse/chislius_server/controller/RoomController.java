package ru.hse.chislius_server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.chislius_server.dto.room.CreatePrivateRoomRequest;
import ru.hse.chislius_server.dto.room.RoomCodeResponse;
import ru.hse.chislius_server.dto.room.RoomResponse;
import ru.hse.chislius_server.service.RoomService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    private final RoomService roomService;

    @GetMapping("/{code}")
    public RoomResponse get(@PathVariable String code) {
        return roomService.getByCode(code);
    }

    @PostMapping
    public RoomCodeResponse createPrivate(CreatePrivateRoomRequest request) {
        return roomService.createPrivate(request);
    }

    @PostMapping("/{code}/join")
    public RoomCodeResponse joinPrivate(@PathVariable String code) {
        return roomService.joinPrivate(code);
    }

    @PostMapping("/join")
    public RoomCodeResponse joinPublic() {
        return roomService.joinPublic();
    }

    @DeleteMapping("/{code}")
    public void delete(@PathVariable String code) {
        roomService.delete(code);
    }

    @PostMapping("/ping")
    public void ping() {
        roomService.ping();
    }
}
