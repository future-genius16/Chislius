package ru.hse.chislius_server.room.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.hse.chislius_server.room.dto.CreatePrivateRoomRequest;
import ru.hse.chislius_server.room.dto.RoomCodeResponse;
import ru.hse.chislius_server.room.dto.RoomResponse;
import ru.hse.chislius_server.room.dto.RoomsUpdateResponse;
import ru.hse.chislius_server.room.exception.UnableConnectRoomException;
import ru.hse.chislius_server.room.exception.UnableCreateRoomException;
import ru.hse.chislius_server.room.model.AbstractRoom;
import ru.hse.chislius_server.room.model.PrivateRoom;
import ru.hse.chislius_server.room.model.PublicRoom;
import ru.hse.chislius_server.user.User;
import ru.hse.chislius_server.user.service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RequiredArgsConstructor
@Service
@Slf4j
public class RoomService {
    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;

    @Getter
    private final Map<String, AbstractRoom> roomMap = new HashMap<>();

    @Value("${config.room.capacity.public}")
    private int PUBLIC_ROOM_CAPACITY;

    @Value("${config.room.capacity.private}")
    private int PRIVATE_ROOM_CAPACITY;

    private AbstractRoom lastPublicRoom;
    private final Random random = new Random();

    public void ping() {
        broadcastRooms();
    }

    public RoomCodeResponse createPrivateRoom(CreatePrivateRoomRequest request) {
        User owner = userService.getCurrentUser();
        PrivateRoom room = new PrivateRoom(PRIVATE_ROOM_CAPACITY, owner);
        addRoomToMap(room);
        String roomId = room.join(owner);
        broadcastRooms();
        return new RoomCodeResponse(roomId);
    }

    public RoomCodeResponse joinPrivateRoom(String roomId) {
        User user = userService.getCurrentUser();
        AbstractRoom room = get(roomId);
        if (room.isOpen()) {
            throw new UnableConnectRoomException("Can connect only private room");
        }
        roomId = room.join(user);
        broadcastRooms();
        return new RoomCodeResponse(roomId);
    }

    public RoomCodeResponse joinPublicRoom() {
        User user = userService.getCurrentUser();
        if (lastPublicRoom == null || lastPublicRoom.isStarted()) {
            lastPublicRoom = new PublicRoom(PUBLIC_ROOM_CAPACITY);
            addRoomToMap(lastPublicRoom);
        }
        String roomId = lastPublicRoom.join(user);
        broadcastRooms();
        return new RoomCodeResponse(roomId);
    }

    public RoomResponse getRoom(String roomId) {
        return new RoomResponse(get(roomId));
    }

    public void deleteRoom(String roomId) {
        AbstractRoom room = get(roomId);
        roomMap.remove(roomId);
    }

    private AbstractRoom get(String roomId) {
        if (!roomMap.containsKey(roomId)) {
            throw new UnableConnectRoomException("AbstractRoom is not exist");
        }
        return roomMap.get(roomId);
    }

    private synchronized void addRoomToMap(AbstractRoom room) {
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

    private void broadcastRooms() {
        messagingTemplate.convertAndSend("/topic/rooms-updates", new RoomsUpdateResponse(new ArrayList<>()));
        log.info("Send rooms broadcast {}", roomMap);
    }
}
