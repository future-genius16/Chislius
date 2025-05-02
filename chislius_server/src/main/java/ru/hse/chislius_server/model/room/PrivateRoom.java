package ru.hse.chislius_server.model.room;

import ru.hse.chislius_server.model.User;

public class PrivateRoom extends Room {
    private User owner;

    public PrivateRoom(int capacity, User owner) {
        super(capacity, false);
        this.owner = owner;
    }

    @Override
    protected void onJoin(User user) {

    }

    @Override
    protected void onLeave(User user) {
        if (users.size() > 1) {
            owner = users.stream().toList().get(0);
        }
    }
}
