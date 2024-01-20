package com.example.learnhub.DTO.user.response;


import com.example.learnhub.Entity.Role;
import com.example.learnhub.Entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserResponse {
    private Integer id;
    private String email;
    private String fullname;
    private String faccebook;
    private String image;
    private String role;
    private Boolean enable;

    public UserResponse(User user, Role role) {
        this.id = user.getUserId();
        this.email = user.getEmail();
        this.fullname = user.getFullName();
        this.faccebook = user.getFacebook();
        this.image = user.getImage();
        this.role = role == null ? null : role.getRoleName();
        this.enable = user.getEnable();
    }
}
