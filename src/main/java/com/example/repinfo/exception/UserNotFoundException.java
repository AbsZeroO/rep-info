package com.example.repinfo.exception;

public class UserNotFoundException extends RuntimeException {
    private final String userName;

    public UserNotFoundException(String userName) {
        super("User not found: " + userName);
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}