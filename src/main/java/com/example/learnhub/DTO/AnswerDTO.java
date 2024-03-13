package com.example.learnhub.DTO;

import com.example.learnhub.Entity.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AnswerDTO {
    private Integer answerId;

    @NotNull
    private Question question;

    @NotNull
    @Size(max = 255)
    private String answerText;

    @NotNull
    private Boolean isCorrect;

    public AnswerDTO(Integer id, String text, boolean correct) {
    }
}
