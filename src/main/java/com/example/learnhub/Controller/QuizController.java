package com.example.learnhub.Controller;

import com.example.learnhub.DTO.QuizDTO;
import com.example.learnhub.Entity.Quiz;
import com.example.learnhub.Service.ServiceOfQuiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// QuizController.java
@RestController
@RequestMapping("/quiz")
public class QuizController {

    private final ServiceOfQuiz quizService;
    @Autowired
    public QuizController(ServiceOfQuiz quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/create")
    public ResponseEntity<Quiz> createQuiz(@RequestBody QuizDTO quizDTO) {
        Quiz createdQuiz = quizService.createQuiz(quizDTO);
        return new ResponseEntity<>(createdQuiz, HttpStatus.CREATED);
    }


    // Add other endpoints for quiz management (update, delete, get by ID, etc.)
}



