package com.example.learnhub.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "QuizAttempt")
public class QuizAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AttemptID")
    private Integer attemptId;

    @ManyToOne
    @JoinColumn(name = "QuizID")
    private Quiz quiz;

    @ManyToOne
    @JoinColumn(name = "UserID")
    private User user;

    @Column(name = "StartTime")
    private Date startTime;

    @Column(name = "EndTime")
    private Date endTime;

}
