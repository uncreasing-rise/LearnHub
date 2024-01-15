package com.example.learnhub.Entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Builder
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, name = "UserID")
    private Integer userId;

    @Column(nullable = false, unique = true, name = "UserName")
    private String userName;

    @Column(nullable = false, name = "UserPassword")
    private String userPassword;

    @Column(nullable = false, name = "Image")
    private String image;

    @Column(nullable = false, name = "Facebook")
    private String facebook;

    @Column(nullable = false, name = "Email")
    private String email;

    @Column(nullable = false, name = "FullName")
    private String fullName;

    @ManyToOne
    @JoinColumn(name = "RoleID")
    private Role role;

    // Constructors, getters, setters, and other methods as needed
}
