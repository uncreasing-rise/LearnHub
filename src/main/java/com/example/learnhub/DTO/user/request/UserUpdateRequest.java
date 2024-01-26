package com.example.learnhub.DTO.user.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserUpdateRequest {
    private String fullname;
    private String facebook;
    private String image;
    private Integer roleId;
    private Boolean enable;

}
