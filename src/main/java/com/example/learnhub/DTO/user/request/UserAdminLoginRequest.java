package com.example.learnhub.DTO.user.request;

import groovy.transform.Sealed;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserAdminLoginRequest {
    private String email;
    @ToString.Exclude
    private String password;
}
