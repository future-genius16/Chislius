package ru.hse.chislius_server.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.hse.chislius_server.dto.room.RoomResponse;
import ru.hse.chislius_server.model.User;
import ru.hse.chislius_server.model.room.Room;

@RequiredArgsConstructor
@Component
public class RoomMapper {
    public RoomResponse toRoomResponse(Room room) {
        return new RoomResponse(room.getCode(), room.getType(), room.getCapacity(), room.getState(), room.getUsers().stream().map(User::getUsername).toList());
    }
}
