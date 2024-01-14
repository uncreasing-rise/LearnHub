package com.example.learnhub.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Question")
public class Question {
    @Id
    @Column(name = "QuestionID")
    private Integer questionId;

    @Column(name = "QuizID")
    private Integer quizId;

    @Column(name = "Point")
    private Integer point;

    @Column(name = "QuestionText")
    private String questionText;

}
