package ru.hse.chislius_server.room.exception;

public class UnableConnectRoomException extends RuntimeException {
    public UnableConnectRoomException(String message) {
        super(message);
    }
}
