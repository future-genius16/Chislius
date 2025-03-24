package ru.hse.chislius_server.room.model;

import ru.hse.chislius_server.user.User;

public class PublicRoom extends AbstractRoom {
    public PublicRoom(int capacity) {
        super(capacity, true);
    }

    @Override
    protected void onJoin(User user) {
        if (this.isFull()) {
            onStart();
        }
    }

    @Override
    protected void onLeave(User user) {

    }

}
