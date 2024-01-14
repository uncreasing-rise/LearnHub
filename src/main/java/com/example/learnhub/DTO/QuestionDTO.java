package com.example.learnhub.DTO;

import lombok.Data;

@Data
public class QuestionDTO {
    private Integer questionId;
    private Integer quizId;
    private Integer point;
    private String questionText;

}
