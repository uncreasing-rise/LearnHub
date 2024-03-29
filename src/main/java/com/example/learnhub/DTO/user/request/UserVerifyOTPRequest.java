package com.example.learnhub.DTO.user.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserVerifyOTPRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String otp;
}
