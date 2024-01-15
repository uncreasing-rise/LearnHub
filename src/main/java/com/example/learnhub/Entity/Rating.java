package com.example.learnhub.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "Rating")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RatingID")
    private Integer ratingId;

    @Column(name = "RatingValue")
    private Double ratingValue;

    @ManyToOne
    @JoinColumn(name = "CourseID")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "UserID")
    private User user;

    @Column(name = "RatingTime")
    private Date ratingTime;

}
