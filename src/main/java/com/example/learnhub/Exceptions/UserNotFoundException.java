package com.example.learnhub.Exceptions;

public class UserNotFoundException extends Throwable {
    private final int userId;

    public UserNotFoundException(int userId) {
        super("User not found with ID: " + userId);
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }
}
