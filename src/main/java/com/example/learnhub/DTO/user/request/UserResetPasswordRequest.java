package com.example.learnhub.DTO.user.request;


import groovy.transform.Sealed;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResetPasswordRequest {
    private String email;
}
