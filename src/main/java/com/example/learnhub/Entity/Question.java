package com.example.learnhub.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QuestionID")
    private Integer questionId;

    @ManyToOne
    @JoinColumn(name = "QuizID")
    private Quiz quiz;

    @Column(name = "Point")
    private Integer point;

    @Column(name = "QuestionText")
    private String questionText;

}
