package com.example.learnhub.DTO.quiz.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AnswerRequest {
    @NotNull
    private Integer questionId;
    @NotNull
    private Integer answerId;
}
