package com.example.learnhub.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class QuizDTO {
    private Integer quizId;
    private Integer courseId;
    private String quizTitle;
    private String quizDescription;
    private List<QuestionDTO> questions;
    private SectionDTO sectionDTO;
    public QuizDTO(Integer id, String title, List<QuestionDTO> questionDTOS) {
    }
}
