package com.example.learnhub.Exceptions;

import com.example.learnhub.DTO.common.enums.ErrorMessage;

public class UnauthorizeException extends RuntimeException {
    private final String message;

    public UnauthorizeException(String message) {
        super(message);
        this.message = message;
    }

    public UnauthorizeException(ErrorMessage message) {
        super(message.getMessage());
        this.message = message.getMessage();
    }

}
