package ru.hse.chislius_server.dto.update;

import lombok.Data;
import ru.hse.chislius_server.model.User;
import ru.hse.chislius_server.model.room.Room;

@Data
public class UserResponse {
    private final int id;
    private final String name;
    private final int rating;
    private final int score;

    public UserResponse(User user) {
        id = user.hashCode();
        name = user.getUsername();
        rating = user.getUsername().hashCode();
        Room room = user.getCurrentRoom();
        if (room != null) {
            Integer scoreObj = room.getScores().get(user);
            if (scoreObj != null) {
                score = scoreObj;
            } else {
                score = 0;
            }
        } else {
            score = 0;
        }
    }
}
