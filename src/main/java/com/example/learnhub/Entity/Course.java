package com.example.learnhub.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings = new ArrayList<>();

    @Column(name = "Level")
    private String level;

    @Column(name = "Tag")
    private String tag;

    @Column(name = "UserID")
    private Integer userId;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    @Column(name = "Status")
    private Integer status = 0;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "CategoryID", insertable = false, updatable = false)
    private Category category;

    @OneToOne(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private LearningDetail learningDetail;


    @ManyToMany
    @JoinTable(
            name = "User_Course",
            joinColumns = @JoinColumn(name = "CourseID"),
            inverseJoinColumns = @JoinColumn(name = "UserID")
    )
    private List<User> users = new ArrayList<>();


    // getters and setters
}
