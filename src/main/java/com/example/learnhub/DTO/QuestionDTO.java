package com.example.learnhub.DTO;

import com.example.learnhub.Entity.Quiz;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data@AllArgsConstructor
@NoArgsConstructor
public class QuestionDTO {
    private Integer questionId;
    private Quiz quiz;
    private Integer point;
    private String questionText;
    private List<AnswerDTO> answerDTOs; // added list of AnswerDTO

    public QuestionDTO(Integer id, String text, Integer point, List<AnswerDTO> answerDTOS) {
    }
}
