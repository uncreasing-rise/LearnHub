package com.example.learnhub.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "AnswerAttempt")
public class AnswerAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AttemptID")
    private Integer attemptId;

    @Id
    @Column(name = "QuestionID")
    private Integer questionId;

    @Column(name = "SelectedAnswerID")
    private Integer selectedAnswerId;


}
