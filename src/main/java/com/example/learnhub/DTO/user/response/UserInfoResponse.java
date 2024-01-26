package com.example.learnhub.DTO.user.response;


import com.example.learnhub.Entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserInfoResponse {
    private Integer id;
    private String email;
    private String fullname;

    public UserInfoResponse(User user) {
        this.id = user.getUserId();
        this.fullname = user.getFullName();
        this.email = user.getEmail();
    }
}
