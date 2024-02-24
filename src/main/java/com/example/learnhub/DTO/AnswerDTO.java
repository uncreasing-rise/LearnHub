package com.example.learnhub.DTO;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class AnswerDTO {
    private Integer answerId;

    @NotNull
    private Integer questionId;

    @NotNull
    @Size(max = 255)
    private String answerText;

    @NotNull
    private Boolean isCorrect;
}
