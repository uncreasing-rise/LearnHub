package com.example.learnhub.Service;

import com.example.learnhub.DTO.QuizDTO;
import org.springframework.stereotype.Service;

@Service
public class ServiceOfQuiz {
    public QuizDTO createQuiz(QuizDTO quizDTO) {
        return quizDTO;
    }
}
