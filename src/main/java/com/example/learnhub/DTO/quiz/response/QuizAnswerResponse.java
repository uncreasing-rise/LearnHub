package com.example.learnhub.DTO.quiz.response;


import com.example.learnhub.Entity.AnswerAttempt;
import com.example.learnhub.Entity.Question;
import com.example.learnhub.Entity.Quiz;
import com.example.learnhub.Entity.QuizAttempt;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class QuizAnswerResponse {
    private Integer id;
    private Integer userId;
    private String title;
    private Date startTime;
    private Date endTime;
    private Integer quizId;
    List<Question> questionList;
    List<AnswerAttempt> answerList;
    public Double totalPoint;
    public Double point;

    public QuizAnswerResponse(QuizAttempt quizAttempt) {
        this.id = quizAttempt.getAttemptId();
        this.userId = quizAttempt.getUser().getUserId();
        this.title = quizAttempt.getQuiz().getTitle();
        this.startTime = quizAttempt.getStartTime();
        this.endTime = quizAttempt.getEndTime();
        this.quizId = quizAttempt.getQuiz().getId();
        this.questionList = quizAttempt.getQuiz().getQuestions();
        this.answerList = quizAttempt.getListAnswer();
        this.totalPoint = quizAttempt.getTotalPoint();
        this.point = quizAttempt.getPoint();
    }
}
