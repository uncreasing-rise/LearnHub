package com.example.learnhub.DTO.common.enums;

import lombok.Getter;

@Getter
public enum ErrorMessage {
    USER_ADMIN_INVALID("User Super Admin Invalid"),
    USER_EMAIL_EXISTED("Email existed"),
    USER_EMAIL_INVALID("Email invalid"),
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
    USER_RESET_PASSWORD_FAIL("User reset password fail"),
    USER_OTP_NOT_MATCH("User OTP not match"),
    USER_ROLE_NOT_FOUND("User role not found"),
    USER_DISABLED("User disabled"),
    USER_RESTORE_FAILED("User restore failed"),
    USER_DISABLED_FAILED("User disable failed"),
    USER_UPLOAD_AVATAR_FAILED("User upload avatar failed"),
    USER_CAN_NOT_DELETE_ADMIN("User can not delete Admin"),
    USER_REQUEST_CHANGE_EMAIL_FAIL("Request to change email failed"),
    USER_REQUEST_INVALID("Request invalid"),

    ;

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
