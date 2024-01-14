package com.example.learnhub.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Answer")
public class Answer {
    @Id
    @Column(name = "AnswerID")
    private Integer answerId;

    @Column(name = "QuestionID")
    private Integer questionId;

    @Column(name = "AnswerText")
    private String answerText;

    @Column(name = "IsCorrect")
    private Boolean isCorrect;

}
