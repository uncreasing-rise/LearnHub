package com.example.learnhub.DTO;

import lombok.Data;

@Data
public class AnswerDTO {
    private Integer answerId;
    private Integer questionId;
    private String answerText;
    private Boolean isCorrect;

}
