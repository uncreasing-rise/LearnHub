package com.example.learnhub.Exceptions;

import com.example.learnhub.DTO.common.enums.ErrorMessage;

public class BusinessException extends RuntimeException {

    private final String message;


    private BusinessException(String message) {
        super(message);
        this.message = message;
    }


    public BusinessException(ErrorMessage message) {
        super(message.getMessage());
        this.message = message.getMessage();
    }

}
