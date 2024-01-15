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

    @Column(name = "CategoryID")
    private Integer categoryId;

    @Column(name = "IsPassed")
    private Boolean isPassed;

    @Column(name = "CourseDate")
    @CreationTimestamp
    private Date courseDate;

    @Column(name = "RatingID")
    private Integer ratingId;

    @Column(name = "Level")
    private String level;

    @Column(name = "Tag")
    private String tag;

    @Column(name = "UserID")
    private Integer userId;

    @OneToOne
    @JoinColumn(name = "ImageID")
    private Image image;

    // Constructors, getters, setters, and other methods as needed
}
