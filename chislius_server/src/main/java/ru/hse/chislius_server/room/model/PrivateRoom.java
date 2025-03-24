package ru.hse.chislius_server.room.model;

import ru.hse.chislius_server.user.User;

public class PrivateRoom extends AbstractRoom {
    private User owner;

    public PrivateRoom(int capacity, User owner) {
        super(capacity, false);
        this.owner = owner;
        join(owner);
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
