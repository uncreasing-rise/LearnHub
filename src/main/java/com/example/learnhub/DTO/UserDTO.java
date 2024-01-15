package com.example.learnhub.DTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

    private int userID;

    @NotBlank(message = "User Name is required")
    private String userName;

    @NotBlank(message = "Password cannot be blank")
    private String userPassword;


    private byte[] image;
    private String facebook;

    @NotBlank(message = "Email is required")
    private String email;
    private String fullName;

    @NotNull(message = "RoleID cannot be null")
    private Integer roleId;

}
