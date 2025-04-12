package ru.hse.chislius_server.room.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.hse.chislius_server.room.dto.RoomsUpdateResponse;
import ru.hse.chislius_server.room.exception.UnableConnectRoomException;
import ru.hse.chislius_server.room.exception.UnableCreateRoomException;
import ru.hse.chislius_server.room.model.AbstractRoom;
import ru.hse.chislius_server.room.model.PrivateRoom;
import ru.hse.chislius_server.room.model.PublicRoom;
import ru.hse.chislius_server.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RequiredArgsConstructor
@Service
@Slf4j
public class RoomService {
    private final SimpMessagingTemplate messagingTemplate;

    @Getter
    private final Map<String, AbstractRoom> roomMap = new HashMap<>();

    private final Map<String, User> userMap = new HashMap<>();

    @Value("${config.room.capacity.public}")
    private int PUBLIC_ROOM_CAPACITY;

    @Value("${config.room.capacity.private}")
    private int PRIVATE_ROOM_CAPACITY;

    private AbstractRoom lastPublicRoom;
    private final Random random = new Random();

    public void update() {
        broadcastRooms();
    }

    public String createPrivateRoom(User owner) throws UnableCreateRoomException {
        PrivateRoom room = new PrivateRoom(PRIVATE_ROOM_CAPACITY, owner);
        addRoomToMap(room);
        String code = room.join(owner);
        broadcastRooms();
        return code;
    }

    public String joinPrivateRoom(User user, String code) throws UnableConnectRoomException {
        if (roomMap.containsKey(code)) {
            AbstractRoom room = roomMap.get(code);
            if (!room.isOpen()) {
                code = room.join(user);
                broadcastRooms();
                return code;
            }
        }
        throw new UnableConnectRoomException();
    }

    public String joinPublicRoom(User user) throws UnableCreateRoomException {
        if (lastPublicRoom == null || lastPublicRoom.isStarted()) {
            lastPublicRoom = new PublicRoom(PUBLIC_ROOM_CAPACITY);
            addRoomToMap(lastPublicRoom);
        }
        String code = lastPublicRoom.join(user);
        broadcastRooms();
        return code;
    }

    private synchronized void addRoomToMap(AbstractRoom room) throws UnableCreateRoomException {
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

    public User getUser(String username) {
        if (!userMap.containsKey(username)) {
            userMap.put(username, new User(username));
        }
        return userMap.get(username);
    }

    private void broadcastRooms() {
        messagingTemplate.convertAndSend("/topic/rooms-updates", new RoomsUpdateResponse(new ArrayList<>()));
        log.info("Send rooms broadcast {}", roomMap);
    }
}
