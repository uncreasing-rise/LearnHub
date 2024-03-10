package com.example.learnhub.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
// Validation
@Data
public class CreateUserDTO {
    @NotNull(message = "UserName cannot be null")
    @NotBlank(message = "UserName cannot be empty")
    private String username;

    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password cannot be empty")
    private String password;
    @NotNull(message = "FullName cannot be null")
    @NotBlank(message = "FullName cannot be empty")
    private String fullName;
    private String  image;
    private String email;
    private Integer role_id;
    private String facebook;




}