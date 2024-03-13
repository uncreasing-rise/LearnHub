package com.example.learnhub.DTO.user.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserRegisterRequest {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String fullname;
    @NotBlank
    private String password;

}
