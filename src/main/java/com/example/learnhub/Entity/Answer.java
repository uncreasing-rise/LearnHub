package com.example.learnhub.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Answer")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AnswerID")
    private Integer answerId;

    @ManyToOne
    @JoinColumn(name = "QuestionID", nullable = false)
    private Question question;

    @Column(name = "AnswerText")
    private String answerText;

    @Column(name = "IsCorrect")
    private Boolean isCorrect;

    // Getters and setters
}
