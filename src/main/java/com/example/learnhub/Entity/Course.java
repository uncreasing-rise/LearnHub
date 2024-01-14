package com.example.learnhub.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;
@Data
@Entity
@Table(name = "Course")
public class Course {
    @Id
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
    private Date courseDate;

    @Column(name = "RatingID")
    private Integer ratingId;

    @Column(name = "Level")
    private String level;

    @Column(name = "Tag")
    private String tag;

    @Column(name = "UserID")
    private Integer userId;

}
