package com.example.learnhub.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "AnswerAttempt")
public class AnswerAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ansAttemptID")
    private Integer attemptId;

    @Id
    @Column(name = "QuestionID")
    private Integer questionId;

    @Column(name = "SelectedAnswerID")
    private Integer selectedAnswerId;

    @Column(name = "answer_correct_id")
    private Integer correctAnswerId;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "quizAttemptID", referencedColumnName = "quizAttemptID")
    private QuizAttempt quizAttempt;


}
