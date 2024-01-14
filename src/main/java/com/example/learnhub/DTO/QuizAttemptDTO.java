package com.example.learnhub.DTO;

import lombok.Data;

import java.util.Date;
@Data
public class QuizAttemptDTO {
    private Integer attemptId;
    private Integer quizId;
    private Integer userId;
    private Date startTime;
    private Date endTime;

}
