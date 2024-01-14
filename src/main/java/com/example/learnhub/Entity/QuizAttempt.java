package com.example.learnhub.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "QuizAttempt")
public class QuizAttempt {
    @Id
    @Column(name = "AttemptID")
    private Integer attemptId;

    @Column(name = "QuizID")
    private Integer quizId;

    @Column(name = "UserID")
    private Integer userId;

    @Column(name = "StartTime")
    private Date startTime;

    @Column(name = "EndTime")
    private Date endTime;

}
