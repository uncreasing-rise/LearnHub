package com.example.learnhub.Entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "[dbo].[User]")  // Assuming the table name in the database is "User"
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, name = "UserID")
    private Integer userId;

    @Column(nullable = false, unique = true, name = "UserName")
    private String userName;

    @Column(nullable = false, name = "UserPassword")
    private String userPassword;

    @Column(nullable = false, name = "Image", columnDefinition = "nvarchar(max)")  // Adjust the columnDefinition
    private String image;

    @Column(nullable = false, name = "Facebook", columnDefinition = "nvarchar(max)")  // Adjust the columnDefinition
    private String facebook;

    @Column(nullable = false, name = "Email", columnDefinition = "nvarchar(max)")  // Adjust the columnDefinition
    private String email;

    @Column(nullable = false, name = "FullName", columnDefinition = "nvarchar(max)")  // Adjust the columnDefinition
    private String fullName;

    @Column(nullable = false, name = "token", columnDefinition = "nvarchar(max)")  // Adjust the columnDefinition
    private String token;

    @Column(nullable = false, name = "roleId")
    private Integer roleId;

    public User(Integer userId, String userName, String userPassword, String image, String facebook, String email, String fullName, String token, Integer roleId) {
        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
        this.image = image;
        this.facebook = facebook;
        this.email = email;
        this.fullName = fullName;
        this.token = token;
        this.roleId = roleId;
    }
}
