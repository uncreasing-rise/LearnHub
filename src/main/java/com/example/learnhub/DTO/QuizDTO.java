package com.example.learnhub.DTO;

import lombok.Data;

@Data
public class QuizDTO {
    private Integer quizId;
    private Integer courseId;
    private String quizTitle;
    private String quizDescription;


}
