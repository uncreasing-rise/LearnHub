package com.example.learnhub.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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


    @Column(name = "course_title")
    private String courseTitle;

    @Column(name = "course_des")
    private String courseDes;

    @Column(name = "course_price")
    private Double coursePrice;

    @Column(name = "is_passed")
    private Boolean isPassed;

    @Column(name = "course_date")
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

    @Column(name = "ImageURL")
    private String imageUrl;

    @Column(name = "VideoURL")
    private String videoUrl;

    @Column(name = "Status")
    private Integer status = 0;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "CategoryID")
    private Category category;

    @OneToOne(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
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
