package com.example.learnhub.Controller;

import com.example.learnhub.DTO.AnswerDTO;
import com.example.learnhub.DTO.QuestionDTO;
import com.example.learnhub.Entity.Question;
import com.example.learnhub.Repository.QuestionRepository;
import com.example.learnhub.Service.ServiceOfQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    private final ServiceOfQuestion questionService;
    private final QuestionRepository questionRepository;


    @Autowired
    public QuestionController(ServiceOfQuestion questionService, QuestionRepository questionRepository) {
        this.questionService = questionService;
        this.questionRepository = questionRepository;
    }

//    @PostMapping("/add")
//    public ResponseEntity<Question> createQuestion(@Valid @RequestBody QuestionDTO questionDTO) {
//        // Call the createQuestion method from questionService
//        Question createdQuestionDTO = questionService.createQuestion(questionDTO);
//
//        // Return the createdQuestionDTO in the response with HTTP status 201
//        return new ResponseEntity<>(createdQuestionDTO, HttpStatus.CREATED);
//    }






}
