package ru.hse.chislius_server.config.context;

import org.springframework.stereotype.Component;

@Component
public class UserContext {
    private final ThreadLocal<String> userTokenHolder = new ThreadLocal<>();

    public void setUserToken(String token) {
        userTokenHolder.set(token);
    }

    public String getUserToken() {
        return userTokenHolder.get();
    }

    public void clear() {
        userTokenHolder.remove();
    }
}
