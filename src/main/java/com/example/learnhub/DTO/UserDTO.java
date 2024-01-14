package com.example.learnhub.DTO;

import lombok.Data;

@Data
public class UserDTO {
    private Integer userId;
    private String userName;
    private String userPassword;
    private String role;
    private String image;
    private String facebook;
    private String email;
    private String fullName;
    private Integer roleId;

}
