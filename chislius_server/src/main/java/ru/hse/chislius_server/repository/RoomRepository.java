package ru.hse.chislius_server.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.hse.chislius_server.model.room.Room;
import ru.hse.chislius_server.model.room.RoomState;
import ru.hse.chislius_server.model.room.RoomType;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RoomRepository {
    private final Set<Room> publicRooms = new HashSet<>();
    private final Set<Room> privateRooms = new HashSet<>();

    private final Map<String, Room> codeIndex = new HashMap<>();
    private final Map<Long, Room> userIdIndex = new HashMap<>();

    public Optional<Room> findNotStartedPublicRoom() {
        return publicRooms.stream()
                .filter((r) -> r.getType() == RoomType.PUBLIC && r.getState() == RoomState.WAIT && r.getUserIds().size() < r.getCapacity())
                .findAny();
    }

    public void saveRoom(Room room) {
        if (room.getType() == RoomType.PUBLIC) {
            publicRooms.add(room);
        } else if (room.getType() == RoomType.PRIVATE) {
            privateRooms.add(room);
        }
        codeIndex.put(room.getCode(), room);
    }

    public void deleteRoom(Room room) {
        publicRooms.remove(room);
        privateRooms.remove(room);
        codeIndex.remove(room.getCode());
    }

    public Optional<Room> findRoomByUserId(long userId) {
        return Optional.ofNullable(userIdIndex.get(userId));
    }

    public Optional<Room> findRoomByCode(String code) {
        return Optional.ofNullable(codeIndex.get(code));
    }

    public void createUserIdIndex(long userId, Room room) {
        userIdIndex.put(userId, room);
    }

    public void deleteUserIdFromIndex(long userId) {
        userIdIndex.remove(userId);
    }


}
