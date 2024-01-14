package com.example.learnhub.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "Rating")
public class Rating {
    @Id
    @Column(name = "RatingID")
    private Integer ratingId;

    @Column(name = "RatingValue")
    private Double ratingValue;

    @Column(name = "CourseID")
    private Integer courseId;

    @Column(name = "UserID")
    private Integer userId;

    @Column(name = "RatingTime")
    private Date ratingTime;

}
