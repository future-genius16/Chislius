package ru.hse.chislius_server.model.room;

import ru.hse.chislius_server.model.User;

public class PublicRoom extends Room {
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
