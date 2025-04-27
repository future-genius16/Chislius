package ru.hse.chislius_server.user.exception;

public class UnableRegisterUserException extends RuntimeException {
    public UnableRegisterUserException(String message) {
        super(message);
    }
}
