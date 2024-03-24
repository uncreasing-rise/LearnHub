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
    private String role;
    public UserInfoResponse(User user) {
        this.id = user.getUserId();
        this.fullname = user.getFullName();
        this.email = user.getEmail();
// Assuming user.getRoleId() returns an int representing the roleId
        int roleId = user.getRoleId();
        if (roleId == 1) {
            this.role = "STUDENT";
        } else if (roleId == 3) {
            this.role = "ADMIN";
        } else {
            // Handle other roleId values here, if needed
            this.role = "UNKNOWN"; // For example, set to a default value
        }
    }
}
