package com.example.learnhub.DTO.common.enums;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    USER_ADMIN_INVALID("User Super Admin Invalid"),
    USER_EMAIL_EXISTED("Email existed"),
    USER_EMAIL_FORMAT_INCORRECT("Email incorrect format"),
    USER_CREATE_FAIL("Create user fail"),
    USER_UPDATE_FAIL("Create user fail"),
    USER_ENABLED("User enabled"),
    USER_VERIFY_CHALLENGE_FAILED("User challenge failed"),

    USER_PERMISSION_INVALID("User type invalid"),
    USER_DO_NOT_PERMISSION("User do not permission"),
    USER_NOT_FOUND("User not found"),
    USER_GET_FAIL("User get fail"),
    USER_LOGIN_FAIL("User login fail"),
    USER_PASSWORD_INCORRECT("User password incorrect"),
    USER_AUTHORIZATION_FAILED("User authorization fail"),
    USER_PERMISSION_NOT_FOUND("User permission not found"),
    USER_EMAIL_NOT_MATCH("User email not match"),


    ;

    private String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
