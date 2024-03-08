package com.example.learnhub.DTO;

import lombok.Data;

import java.util.List;

@Data
public class QuizDTO {
    private Integer quizId;
    private Integer courseId;
    private String quizTitle;
    private String quizDescription;
    private List<QuestionDTO> questions;
    private SectionDTO sectionDTO;

}
