package com.example.learnhub.DTO.user.response;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class UserLoginResponse {
    private String accessToken;
    private String refreshToken;
}
