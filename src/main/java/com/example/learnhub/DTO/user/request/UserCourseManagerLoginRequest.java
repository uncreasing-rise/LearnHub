package com.example.learnhub.DTO.user.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserCourseManagerLoginRequest {
    private String email;
    @ToString.Exclude
    private String password;
}
