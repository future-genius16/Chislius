package ru.hse.chislius_server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.hse.chislius_server.dto.update.MessageType;
import ru.hse.chislius_server.exception.DataValidationException;
import ru.hse.chislius_server.exception.EntityNotFoundException;
import ru.hse.chislius_server.exception.GenerationTimeoutException;
import ru.hse.chislius_server.game.entity.Game;
import ru.hse.chislius_server.game.models.GameMode;
import ru.hse.chislius_server.game.service.GameService;
import ru.hse.chislius_server.model.room.Room;
import ru.hse.chislius_server.model.room.RoomState;
import ru.hse.chislius_server.model.room.RoomType;
import ru.hse.chislius_server.repository.RoomRepository;
import ru.hse.chislius_server.repository.UserRepository;

import java.util.Collections;
import java.util.Random;

@RequiredArgsConstructor
@Service
@Slf4j
public class RoomService {
    private final RoomRepository roomRepository;
    private final GameService gameService;
    private final UpdateService updateService;
    private final UserRepository userRepository;

    @Value("${config.room.capacity.public}")
    private int PUBLIC_ROOM_CAPACITY;

    @Value("${config.room.capacity.private.min}")
    private int PRIVATE_ROOM_CAPACITY_MIN;

    @Value("${config.room.capacity.private.max}")
    private int PRIVATE_ROOM_CAPACITY_MAX;

    public Room createPrivateRoom(long userId, int capacity, int gameMode) {
        validateUserNotInRoom(userId);
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
        joinRoom(userId, room);
        broadcastRoom(room);
        return room;
    }

    public Room joinPrivateRoom(long userId, String code) {
        validateUserNotInRoom(userId);
        Room room = getByCode(code);
        if (room.getType() != RoomType.PRIVATE) {
            throw new DataValidationException("По коду возможно присоединиться только к приватной комнате");
        }
        joinRoom(userId, room);
        broadcastRoom(room);
        return room;
    }

    public Room joinPublicRoom(long userId) {
        validateUserNotInRoom(userId);
        Room room = roomRepository.findNotStartedPublicRoom().orElseGet(() -> {
            Room newRoom = new Room(RoomType.PUBLIC, PUBLIC_ROOM_CAPACITY, GameMode.MEDIUM);
            saveRoom(newRoom);
            newRoom.setState(RoomState.WAIT);
            return newRoom;
        });
        joinRoom(userId, room);
        broadcastRoom(room);
        return room;
    }

    public void leaveRoom(long userId) {
        Room room = roomRepository.findRoomByUserId(userId).orElse(null);
        if (room == null || !room.getUserIds().remove(userId)) {
            return;
        }
        roomRepository.deleteUserIdFromIndex(userId);
        updateService.sendUpdate(userId);
        updateService.sendMessage(userId, MessageType.LEAVE, userId);
        if (room.getUserIds().isEmpty()) {
            deleteRoom(room);
            return;
        }
        if (room.getCurrentPlayerId() == userId) {
            room.setCurrentPlayerId(room.getPlayerIds().removeLast());
        } else {
            room.getPlayerIds().remove(userId);
        }
        if (room.getState() != RoomState.FINISH) {
            notifyRoom(room, MessageType.LEAVE, userId);
            broadcastRoom(room);
        }
    }

    public void flipCard(long userId, int id) {
        Room room = getCurrentRoom(userId);
        if (room.getCurrentPlayerId() == userId) {
            if (!gameService.openCard(room.getGame(), id - 1)) {
                throw new DataValidationException("Вы не можете перевернуть эту карту");
            }
            broadcastRoom(room);
        } else {
            throw new DataValidationException("Сейчас не ваш ход");
        }
    }

    public void submitMove(long userId) {
        Room room = getCurrentRoom(userId);
        if (room.getCurrentPlayerId() == userId) {
            int d = gameService.doMove(room.getGame());
            if (d == 0) {
                throw new DataValidationException("Требуется выбрать минимум 2 карты");
            }
            room.getScores().put(userId, room.getScores().get(userId) + d);
            room.getPlayerIds().add(0, userId);
            room.setCurrentPlayerId(room.getPlayerIds().removeLast());
            checkGameOver(room);
            broadcastRoom(room);
            if (d > 0) {
                notifyRoom(room, MessageType.SUCCESS, userId, d);
            } else {
                notifyRoom(room, MessageType.FAIL, userId, -d);
            }
        } else {
            throw new DataValidationException("Сейчас не ваш ход");
        }
    }

    public void skipMove(long userId) {
        Room room = getCurrentRoom(userId);
        if (room.getCurrentPlayerId() == userId) {
            gameService.skipMove(room.getGame());
            room.getPlayerIds().add(0, userId);
            room.setCurrentPlayerId(room.getPlayerIds().removeLast());
            broadcastRoom(room);
            notifyRoom(room, MessageType.SKIP, userId);
        } else {
            throw new DataValidationException("Сейчас не ваш ход");
        }
    }

    private Room getCurrentRoom(long userId) {
        return roomRepository.findRoomByUserId(userId)
                .orElseThrow(() -> new DataValidationException("Вы должны войти в комнату, чтобы совершить это дейcтвие"));
    }

    private Room getByCode(String code) {
        return roomRepository.findRoomByCode(code).orElseThrow(() -> new EntityNotFoundException("Комната не найдена"));
    }

    private void saveRoom(Room room) {
        int counter = 0;
        while (counter < 10) {
            String code = generateRoomCode();
            if (roomRepository.findRoomByCode(code).isEmpty()) {
                room.setCode(code);
                room.setState(RoomState.WAIT);
                roomRepository.saveRoom(room);
                return;
            } else {
                counter++;
            }
        }
        throw new GenerationTimeoutException();
    }

    private void joinRoom(long userId, Room room) {
        if (room.getState() != RoomState.WAIT) {
            throw new DataValidationException("В комнате уже началась игра");
        }
        if (room.getUserIds().size() == room.getCapacity()) {
            throw new DataValidationException("В комнате нет свободных мест");
        }
        room.getUserIds().add(userId);
        notifyRoom(room, MessageType.JOIN, userId);
        if (room.getUserIds().size() == room.getCapacity()) {
            startGame(room);
        }
        roomRepository.createUserIdIndex(userId, room);
    }

    private void deleteRoom(Room room) {
        roomRepository.deleteRoom(room);
        room.setState(RoomState.DELETE);
    }

    public void broadcastRoom(Room room) {
        room.getUserIds().forEach((userId) -> updateService.sendUpdate(userId, room));
    }

    private void notifyRoom(Room room, MessageType type, long userId) {
        room.getUserIds().forEach((destinationId) -> updateService.sendMessage(destinationId, type, userId));
    }

    private void notifyRoom(Room room, MessageType type, long userId, int score) {
        room.getUserIds().forEach((destinationId) -> updateService.sendMessage(destinationId, type, userId, score));
    }

    private String generateRoomCode() {
        Random random = new Random();
        int number = random.nextInt(1000000);
        return String.format("%06d", number);
    }

    private void validateUserNotInRoom(long userId) {
        Room room = roomRepository.findRoomByUserId(userId).orElse(null);
        if (room != null) {
            throw new DataValidationException("Вы должны выйти из комнаты, чтобы совершить это дейcтвие");
        }
    }

    private void startGame(Room room) {
        room.setState(RoomState.IN_PROGRESS);
        Game game = gameService.createGame(room.getCode(), room.getMode());
        room.setGame(game);
        room.getPlayerIds().addAll(room.getUserIds());
        Collections.shuffle(room.getPlayerIds());
        room.setCurrentPlayerId(room.getPlayerIds().removeLast());
        room.getUserIds().forEach((userId) -> room.getScores().put(userId, 0));
        checkGameOver(room);
        broadcastRoom(room);
    }

    private void checkGameOver(Room room) {
        if (!gameService.canMove(room.getGame())) {
            room.setState(RoomState.FINISH);
            room.getScores().forEach((userId, score) -> userRepository.findById(userId).ifPresent(user -> {
                user.setRating(user.getRating() + score);
                if (user.getRating() < 0) {
                    user.setRating(0);
                }
                userRepository.save(user);
            }));
        }
    }
}
