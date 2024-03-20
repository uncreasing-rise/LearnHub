package com.example.learnhub.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "QUIZ_ATTEMPT")
public class QuizAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attemptid")
    private Integer attemptId;

    @ManyToOne
    @JoinColumn(name = "QuizID")
    private Quiz quiz;

    @ManyToOne
    @JoinColumn(name = "UserID")
    private User user;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "end_time")
    private Date endTime;

    @OneToMany(mappedBy = "quizAttempt", cascade = CascadeType.ALL)
    private List<AnswerAttempt> listAnswer;


    private Double totalPoint;
    private Double point;



}
