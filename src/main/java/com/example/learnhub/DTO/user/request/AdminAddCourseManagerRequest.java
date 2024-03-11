package com.example.learnhub.DTO.user.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@ToString
public class AdminAddCourseManagerRequest {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String fullname;
    @NotBlank
    private String password;
}
