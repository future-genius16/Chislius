package ru.hse.chislius_server.room.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.hse.chislius_server.room.exception.UnableCreateRoomException;
import ru.hse.chislius_server.room.model.AbstractRoom;
import ru.hse.chislius_server.room.model.PrivateRoom;
import ru.hse.chislius_server.room.model.PublicRoom;
import ru.hse.chislius_server.user.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class RoomService {
    @Getter
    private final Map<String, AbstractRoom> roomMap = new HashMap<>();

    @Value("${config.room.capacity.public}")
    private int PUBLIC_ROOM_CAPACITY;

    @Value("${config.room.capacity.private}")
    private int PRIVATE_ROOM_CAPACITY;

    private AbstractRoom lastPublicRoom;
    private final Random random = new Random();

    public String joinPublicRoom(User user) throws UnableCreateRoomException {
        if (lastPublicRoom == null || lastPublicRoom.isStarted()) {
            lastPublicRoom = new PublicRoom(PUBLIC_ROOM_CAPACITY);
            addRoomToMap(lastPublicRoom);
        }
        return lastPublicRoom.join(user);
    }

    public String createPrivateRoom(User owner) throws UnableCreateRoomException {
        PrivateRoom room = new PrivateRoom(PRIVATE_ROOM_CAPACITY, owner);
        addRoomToMap(room);
        return room.getCode();
    }

    public String joinPrivateRoom(User user, String code) {
        if (roomMap.containsKey(code)) {
            AbstractRoom room = roomMap.get(code);
            if (!room.isOpen()) {
                return room.join(user);
            }
        }
        return null;
    }

    public synchronized void addRoomToMap(AbstractRoom room) throws UnableCreateRoomException {
        int counter = 0;
        while (counter < 10) {
            String code = generateRoomCode();
            if (!roomMap.containsKey(code)) {
                roomMap.put(code, room);
                room.setCode(code);
                return;
            } else {
                counter++;
            }
        }
        throw new UnableCreateRoomException();
    }

    private String generateRoomCode() {
        int number = random.nextInt(1000000);
        return String.format("%06d", number);
    }
}
