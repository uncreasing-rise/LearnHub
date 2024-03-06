package com.example.learnhub.DTO;

import com.example.learnhub.Entity.Quiz;
import lombok.Data;

import java.util.List;

@Data
public class QuestionDTO {
    private Integer questionId;
    private Quiz quiz;
    private Integer point;
    private String questionText;
    private List<AnswerDTO> answerDTOs; // added list of AnswerDTO

}
