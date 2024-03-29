package com.example.learnhub.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
//@Table(name = "[dbo].[User]")  // Assuming the table name in the database is "User"
@Table(name = "users")  // Assuming the table name in the database is "User" . Do not delete this comment
@Accessors(chain = true)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, name = "UserID")
    private Integer userId;

    @Column(nullable = false, name = "user_password")
    private String userPassword;

    @Column(name = "ImageURL")
    private String image;  // Representing image as a URL string


    @Column(nullable = false, name = "Email", columnDefinition = "nvarchar(max)")
    private String email;

    @Column(nullable = false, name = "FullName", columnDefinition = "nvarchar(max)")
    private String fullName;

    @Column(nullable = false, name = "token", columnDefinition = "nvarchar(max)")
    private String token;

    @Column(nullable = false, name = "role_id")
    private Integer roleId;

    @Column(name = "enable", nullable = false)
    private Boolean enable;

    private String stringRandom;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Wishlist> wishlists;
    private Boolean deleted = Boolean.FALSE;

    @ManyToMany
    @JoinTable(
            name = "User_Course",
            joinColumns = @JoinColumn(name = "UserID"),
            inverseJoinColumns = @JoinColumn(name = "CourseID")
    )
    private List<Course> courses = new ArrayList<>();

    public User(Integer userId, String userPassword, String image, String facebook, String email, String fullName, String token, Integer roleId) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.image = image;
        this.email = email;
        this.fullName = fullName;
        this.token = token;
        this.roleId = roleId;
    }

    

    // Helper method to add a course to the user
}