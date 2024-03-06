package com.example.learnhub.DTO;

import com.example.learnhub.Entity.Question;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
}
