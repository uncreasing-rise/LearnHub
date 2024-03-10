package com.example.learnhub.Controller;


import com.example.learnhub.Service.ServiceOfQuiz;
import org.springframework.beans.factory.annotation.Autowired;
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



    // Add other endpoints for quiz management (update, delete, get by ID, etc.)
}



