package ru.hse.chislius_server.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.hse.chislius_server.dto.room.CreatePrivateRoomRequest;
import ru.hse.chislius_server.dto.room.RoomCodeResponse;
import ru.hse.chislius_server.dto.room.RoomResponse;
import ru.hse.chislius_server.dto.room.RoomsUpdateResponse;
import ru.hse.chislius_server.exception.DataValidationException;
import ru.hse.chislius_server.exception.EntityNotFoundException;
import ru.hse.chislius_server.exception.GenerationTimeoutException;
import ru.hse.chislius_server.model.room.Room;
import ru.hse.chislius_server.model.room.PrivateRoom;
import ru.hse.chislius_server.model.room.PublicRoom;
import ru.hse.chislius_server.model.User;

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
    private final Map<String, Room> roomMap = new HashMap<>();

    @Value("${config.room.capacity.public}")
    private int PUBLIC_ROOM_CAPACITY;

    @Value("${config.room.capacity.private}")
    private int PRIVATE_ROOM_CAPACITY;

    private Room lastPublicRoom;
    private final Random random = new Random();

    public RoomResponse getByCode(String code) {
        return new RoomResponse(get(code));
    }

    public RoomCodeResponse createPrivate(CreatePrivateRoomRequest request) {
        User owner = userService.getCurrentUser();
        PrivateRoom room = new PrivateRoom(PRIVATE_ROOM_CAPACITY, owner);
        addRoomToMap(room);
        String code = room.join(owner);
        broadcastRooms();
        return new RoomCodeResponse(code);
    }

    public RoomCodeResponse joinPrivate(String code) {
        User user = userService.getCurrentUser();
        Room room = get(code);
        if (room.isOpen()) {
            throw new DataValidationException("Can connect only private room");
        }
        code = room.join(user);
        broadcastRooms();
        return new RoomCodeResponse(code);
    }

    public RoomCodeResponse joinPublic() {
        User user = userService.getCurrentUser();
        if (lastPublicRoom == null || lastPublicRoom.isStarted()) {
            lastPublicRoom = new PublicRoom(PUBLIC_ROOM_CAPACITY);
            addRoomToMap(lastPublicRoom);
        }
        String roomId = lastPublicRoom.join(user);
        broadcastRooms();
        return new RoomCodeResponse(roomId);
    }

    public void delete(String code) {
        get(code);
        roomMap.remove(code);
    }

    public void ping() {
        broadcastRooms();
    }

    private Room get(String code) {
        if (!roomMap.containsKey(code)) {
            throw new EntityNotFoundException("Room not found");
        }
        return roomMap.get(code);
    }

    private synchronized void addRoomToMap(Room room) {
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
        throw new GenerationTimeoutException();
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
