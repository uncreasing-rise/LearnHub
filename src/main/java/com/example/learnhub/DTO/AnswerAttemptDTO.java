package com.example.learnhub.DTO;

import lombok.Data;

@Data
public class AnswerAttemptDTO {
    private Integer attemptId;
    private Integer questionId;
    private Integer selectedAnswerId;


}
