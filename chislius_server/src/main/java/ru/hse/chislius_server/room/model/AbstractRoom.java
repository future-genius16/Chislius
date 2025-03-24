package ru.hse.chislius_server.room.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.hse.chislius_server.user.User;

import java.util.HashSet;
import java.util.Set;

@ToString
public abstract class AbstractRoom {
    protected final Set<User> users = new HashSet<>();
    private final int capacity;

    @Getter
    private final boolean open;

    @Getter
    @Setter
    private RoomState state;

    @Getter
    @Setter
    private String code;

    public AbstractRoom(int capacity, boolean open) {
        this.capacity = capacity;
        this.open = open;
        this.state = RoomState.WAITING_FOR_PLAYERS;
    }

    public String join(User user) {
        if (!isStarted() && users.size() < capacity && users.add(user)) {
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
