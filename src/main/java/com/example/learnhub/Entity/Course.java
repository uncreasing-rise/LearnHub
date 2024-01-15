package com.example.learnhub.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@Entity
@Table(name = "Course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CourseID")
    private Integer courseId;

    @Column(name = "CourseTitle")
    private String courseTitle;

    @Column(name = "CourseDes")
    private String courseDes;

    @Column(name = "CoursePrice")
    private Double coursePrice;

    @ManyToOne
    @JoinColumn(name = "CategoryID")
    private Category category;

    @Column(name = "IsPassed")
    private Boolean isPassed;

    @Column(name = "CourseDate")
    @CreationTimestamp
    private Date courseDate;

    @ManyToOne
    @JoinColumn(name = "RatingID")
    private Rating rating;

    @Column(name = "Level")
    private String level;

    @Column(name = "Tag")
    private String tag;

    @ManyToOne
    @JoinColumn(name = "UserID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "WishlistID")
    private Wishlist wishlist;


    // Constructors, getters, setters, and other methods as needed
}
