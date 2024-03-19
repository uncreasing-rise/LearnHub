package com.example.learnhub.DTO.quiz.request;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class SubmitAnswerRequest {
    private Integer quizId;
    private String principal;
    @NotNull
    private List<AnswerRequest> answerRequests;
    @NonNull
    private Date startTime;
    @NotNull
    private Date endTime;
}
