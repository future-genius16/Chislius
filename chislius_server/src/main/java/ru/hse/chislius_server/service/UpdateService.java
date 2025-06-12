package ru.hse.chislius_server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.hse.chislius_server.dto.update.GameResponse;
import ru.hse.chislius_server.dto.update.Message;
import ru.hse.chislius_server.dto.update.MessageType;
import ru.hse.chislius_server.dto.update.RoomUpdate;
import ru.hse.chislius_server.dto.update.UpdateResponse;
import ru.hse.chislius_server.dto.update.UserMessageResponse;
import ru.hse.chislius_server.dto.update.UserResponse;
import ru.hse.chislius_server.exception.DataValidationException;
import ru.hse.chislius_server.game.entity.Game;
import ru.hse.chislius_server.model.User;
import ru.hse.chislius_server.model.room.Room;
import ru.hse.chislius_server.model.room.RoomState;
import ru.hse.chislius_server.model.room.RoomType;
import ru.hse.chislius_server.repository.RoomRepository;
import ru.hse.chislius_server.repository.UserRepository;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UpdateService {
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    private final SimpMessagingTemplate simpMessagingTemplate;

    public void sendUpdate(long userId) {
        User user = userRepository.findById(userId).orElse(null);
        Room room = roomRepository.findRoomByUserId(userId).orElse(null);
        sendUpdate(user, room);
    }

    public void sendUpdate(long userId, Room room) {
        User user = userRepository.findById(userId).orElse(null);
        sendUpdate(user, room);
    }

    private void sendUpdate(User user, Room room) {
        int tmpState = 0;
        RoomUpdate tmpData = null;
        if (room != null) {
            if (room.getState() == RoomState.WAIT) {
                tmpState = 1;
            } else if (room.getState() == RoomState.IN_PROGRESS) {
                if (room.getCurrentPlayerId() != user.getId()) {
                    tmpState = 2;
                } else {
                    tmpState = 3;
                }
            } else if (room.getState() == RoomState.FINISH) {
                tmpState = 4;
            }

            tmpData = getRoomUpdate(room);
        }

        UpdateResponse updateResponse = new UpdateResponse(tmpState, getUserResponse(user.getId(), room), tmpData);

        simpMessagingTemplate.convertAndSendToUser(user.getToken(), "/update", updateResponse);
    }

    private RoomUpdate getRoomUpdate(Room room) {
        String code = room.getCode();
        boolean open = room.getType() == RoomType.PUBLIC;
        int capacity = room.getCapacity();
        int mode = room.getMode().ordinal();
        List<UserResponse> players;
        UserResponse currentPlayer;

        if (room.getState() == RoomState.WAIT || room.getState() == RoomState.FINISH) {
            players = room.getUserIds().stream().map((userId -> getUserResponse(userId, room))).toList();
            currentPlayer = null;
        } else {
            players = room.getPlayerIds().stream().map(userId -> getUserResponse(userId, room)).toList();
            currentPlayer = getUserResponse(room.getCurrentPlayerId(), room);
        }
        Game game = room.getGame();
        GameResponse board;
        if (game != null) {
            board = new GameResponse(game);
        } else {
            board = null;
        }

        return new RoomUpdate(code, open, capacity, mode, players, currentPlayer, board);
    }

    private UserResponse getUserResponse(long userId, Room room) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DataValidationException("Пользователь не найден"));
        long id = user.getId();
        String name = user.getUsername();
        int avatar = user.getAvatar();
        int rating = user.getRating();
        int score;

        if (room != null) {
            Integer scoreObj = room.getScores().get(user.getId());
            score = Objects.requireNonNullElse(scoreObj, 0);
        } else {
            score = 0;
        }
        return new UserResponse(id, name, avatar, rating, score);
    }

    public void sendMessage(long destinationId, MessageType type, long userId) {
        userRepository.findById(userId)
                .ifPresent(user -> userRepository.findById(destinationId)
                        .ifPresent((destination) -> sendMessage(destination, new Message(type, getUserMessageResponse(user)))));
    }

    public void sendMessage(long destinationId, MessageType type, long userId, int score) {
        userRepository.findById(userId)
                .ifPresent(user -> userRepository.findById(destinationId)
                        .ifPresent((destination) -> sendMessage(destination, new Message(type, getUserMessageResponse(user), score))));
    }

    private void sendMessage(User user, Message message) {
        simpMessagingTemplate.convertAndSendToUser(user.getToken(), "/message", message);
    }

    private UserMessageResponse getUserMessageResponse(User user) {
        return new UserMessageResponse(user.getId(), user.getUsername(), user.getAvatar());
    }
}
