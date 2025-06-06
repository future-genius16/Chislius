package ru.hse.chislius_server.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.hse.chislius_server.exception.DataValidationException;
import ru.hse.chislius_server.exception.EntityNotFoundException;
import ru.hse.chislius_server.exception.GenerationTimeoutException;
import ru.hse.chislius_server.game.entity.Game;
import ru.hse.chislius_server.game.models.GameMode;
import ru.hse.chislius_server.game.service.GameService;
import ru.hse.chislius_server.model.User;
import ru.hse.chislius_server.model.room.Room;
import ru.hse.chislius_server.model.room.RoomState;
import ru.hse.chislius_server.model.room.RoomType;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Slf4j
public class RoomService {
    private final GameService gameService;
    private final UserService userService;

    @Getter
    private final Map<String, Room> codeRoomMap = new HashMap<>();
    private final Set<Room> publicRooms = new HashSet<>();

    @Value("${config.room.capacity.public}")
    private int PUBLIC_ROOM_CAPACITY;

    @Value("${config.room.capacity.private.min}")
    private int PRIVATE_ROOM_CAPACITY_MIN;

    @Value("${config.room.capacity.private.max}")
    private int PRIVATE_ROOM_CAPACITY_MAX;

    public void validateUserNotInRoom(User user) {
        if (user.getCurrentRoom() != null) {
            throw new DataValidationException("Вы должны выйти из комнаты, чтобы совершить это дейcтвие");
        }
    }

    public Room getCurrentRoom(User user) {
        if (user.getCurrentRoom() == null) {
            throw new DataValidationException("Вы должны войти в комнату, чтобы совершить это дейcтвие");
        }
        return user.getCurrentRoom();
    }

    public Room createPrivateRoom(User user, int capacity, int gameMode) {
        if (capacity < PRIVATE_ROOM_CAPACITY_MIN || capacity > PRIVATE_ROOM_CAPACITY_MAX) {
            throw new DataValidationException("Некорректное количество пользователей. Допускается от " + PRIVATE_ROOM_CAPACITY_MIN + " до " + PRIVATE_ROOM_CAPACITY_MAX);
        }
        GameMode mode = switch (gameMode) {
            case 0 -> GameMode.EASY;
            case 1 -> GameMode.MEDIUM;
            case 2 -> GameMode.HARD;
            default -> throw new DataValidationException("Некорректный режим игры");
        };
        Room room = new Room(RoomType.PRIVATE, capacity, mode);
        saveRoom(room);
        joinRoom(room, user);
        broadcastRoom(room);
        return room;
    }

    public Room joinPrivateRoom(User user, String code) {
        Room room = getByCode(code);
        if (room.getType() != RoomType.PRIVATE) {
            throw new DataValidationException("По коду возможно присоединиться только к приватной комнате");
        }
        joinRoom(room, user);
        broadcastRoom(room);
        return room;
    }

    public Room joinPublicRoom(User user) {
        Room room = publicRooms.stream().filter((r) -> r.getType() == RoomType.PUBLIC && r.getState() == RoomState.WAIT && r.getUsers().size() < r.getCapacity()).findAny().orElseGet(() -> {
            Room newRoom = new Room(RoomType.PUBLIC, PUBLIC_ROOM_CAPACITY, GameMode.MEDIUM);
            saveRoom(newRoom);
            newRoom.setState(RoomState.WAIT);
            publicRooms.add(newRoom);
            return newRoom;
        });
        joinRoom(room, user);
        broadcastRoom(room);
        return room;
    }

    public void leaveRoom(User user, Room room) {
        if (!room.getUsers().remove(user)) {
            throw new DataValidationException("Не удалось покинуть комнату");
        }
        userService.sendUpdate(user);
        user.setCurrentRoom(null);
        if (room.getUsers().isEmpty()) {
            deleteRoom(room);
            return;
        }
        if (room.getCurrentPlayer() == user){
            room.setCurrentPlayer(room.getPlayers().removeLast());
        } else {
            room.getPlayers().remove(user);
        }
        broadcastRoom(room);
    }

    private void deleteRoom(Room room) {
        codeRoomMap.remove(room.getCode());
        publicRooms.remove(room);
        room.setState(RoomState.DELETE);
    }

    public void broadcastRoom(Room room) {
        room.getUsers().forEach(userService::sendUpdate);
    }

    private Room getByCode(String code) {
        if (!codeRoomMap.containsKey(code)) {
            throw new EntityNotFoundException("Комната не найдена");
        }
        return codeRoomMap.get(code);
    }

    private void saveRoom(Room room) {
        int counter = 0;
        while (counter < 10) {
            String code = generateRoomCode();
            if (!codeRoomMap.containsKey(code)) {
                codeRoomMap.put(code, room);
                room.setCode(code);
                room.setState(RoomState.WAIT);
                return;
            } else {
                counter++;
            }
        }
        throw new GenerationTimeoutException();
    }

    private String generateRoomCode() {
        Random random = new Random();
        int number = random.nextInt(1000000);
        return String.format("%06d", number);
    }

    private void joinRoom(Room room, User user) {
        if (room.getState() != RoomState.WAIT) {
            throw new DataValidationException("В комнате уже началась игра");
        }
        if (room.getUsers().size() == room.getCapacity()) {
            throw new DataValidationException("В комнате нет свободных мест");
        }
        room.getUsers().add(user);
        if (room.getUsers().size() == room.getCapacity()) {
            startGame(room);
        }
        user.setCurrentRoom(room);
    }

    private void startGame(Room room) {
        room.setState(RoomState.IN_PROGRESS);
        Game game = gameService.createGame(room.getCode(), room.getMode());
        room.setGame(game);
        room.getPlayers().addAll(room.getUsers());
        Collections.shuffle(room.getPlayers());
        room.setCurrentPlayer(room.getPlayers().removeLast());
        room.getUsers().forEach((user) -> room.getScores().put(user, 0));
        broadcastRoom(room);
    }

    public void flipCard(User user, Room room, int id) {
        if (room.getCurrentPlayer() == user) {
            if (!gameService.openCard(room.getGame(), id)){
                throw new DataValidationException("Вы не можете перевернуть эту карту");
            }
            broadcastRoom(room);
        } else {
            throw new DataValidationException("Сейчас не ваш ход");
        }
    }

    public void skipMove(User user, Room room) {
        if (room.getCurrentPlayer() == user) {
            gameService.skipMove(room.getGame());
            room.getPlayers().add(0, user);
            room.setCurrentPlayer(room.getPlayers().removeLast());
            checkGameOver(room);
            broadcastRoom(room);
        } else {
            throw new DataValidationException("Сейчас не ваш ход");
        }
    }

    public void submitMove(User user, Room room) {
        if (room.getCurrentPlayer() == user) {
            int d = gameService.doMove(room.getGame());
            room.getScores().put(user, room.getScores().get(user) + d);
            room.getPlayers().add(0, user);
            room.setCurrentPlayer(room.getPlayers().removeLast());
            checkGameOver(room);
            broadcastRoom(room);
        } else {
            throw new DataValidationException("Сейчас не ваш ход");
        }
    }

    private void checkGameOver(Room room) {
        if (!gameService.canMove(room.getGame())) {
            room.setState(RoomState.FINISH);
        }
    }
}
