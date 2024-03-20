package com.example.learnhub.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "answer_attempt")
public class AnswerAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attemptid")
    private Integer attemptId;

    @Column(name = "QuestionID")
    private Integer questionId;

    @Column(name = "SelectedAnswerID")
    private Integer selectedAnswerId;

    @Column(name = "answer_correct_id")
    private Integer correctAnswerId;


    @ManyToOne
    @JoinColumn(name = "quiz_attempt_id", referencedColumnName = "attemptId")
    private QuizAttempt quizAttempt;



}
