package ru.hse.chislius_server.model.room;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.hse.chislius_server.model.User;

import java.util.ArrayList;
import java.util.List;

@ToString
public abstract class Room {
    protected final List<User> users = new ArrayList<>();
    private final int capacity;

    @Getter
    private final boolean open;

    @Getter
    @Setter
    private RoomState state;

    @Getter
    @Setter
    private String code;

    public Room(int capacity, boolean open) {
        this.capacity = capacity;
        this.open = open;
        this.state = RoomState.WAITING_FOR_PLAYERS;
    }

    public String join(User user) {
        if (!isStarted() && users.size() < capacity && users.add(user)) {
            if (user.getCurrentRoom() != null) {
                user.getCurrentRoom().leave(user);
            }
            user.setCurrentRoom(this);
            onJoin(user);
            return code;
        }
        return null;
    }

    public boolean leave(User user) {
        if (users.remove(user)) {
            onLeave(user);
            return true;
        }
        return false;
    }

    public boolean isFull() {
        return users.size() == capacity;
    }

    public boolean isStarted() {
        return state != RoomState.WAITING_FOR_PLAYERS;
    }

    protected void onStart() {
        this.setState(RoomState.WAITING_FOR_MOVE);
    }

    protected abstract void onJoin(User user);

    protected abstract void onLeave(User user);
}
