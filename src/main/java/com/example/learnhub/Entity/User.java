package com.example.learnhub.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "User")
public class User {
    @Id
    @Column(name = "UserID")
    private Integer userId;

    @Column(name = "UserName")
    private String userName;

    @Column(name = "UserPassword")
    private String userPassword;

    @Column(name = "Role")
    private String role;

    @Column(name = "Image")
    private String image;

    @Column(name = "Facebook")
    private String facebook;

    @Column(name = "Email")
    private String email;

    @Column(name = "FullName")
    private String fullName;

    @Column(name = "RoleID")
    private Integer roleId;

}
