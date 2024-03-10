package com.example.learnhub.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AnswerAttemptDTO {
    private Integer attemptId;
    private Integer questionId;
    private Integer selectedAnswerId;


}
