package com.example.learnhub.DTO.user.response;


import com.example.learnhub.Entity.Role;
import com.example.learnhub.Entity.User;
import com.example.learnhub.utils.FileUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.Objects;

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
    private Boolean deleted;

    public UserResponse(User user, Role role) {
        this.id = user.getUserId();
        this.email = user.getEmail();
        this.fullname = user.getFullName();
        this.faccebook = user.getFacebook();
        this.image = Objects.equals("url", user.getImage()) ? user.getImage() : FileUtils.getFileUrl(user.getImage());
        this.role = role == null ? null : role.getRoleName();
        this.enable = user.getEnable();
        this.deleted = user.getDeleted();
    }
}
