package com.example.learnhub.Controller;

import com.example.learnhub.DTO.AnswerDTO;
import com.example.learnhub.Service.ServiceOfAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/answers")
public class AnswerController {

    private final ServiceOfAnswer serviceOfAnswer;

    public AnswerController(ServiceOfAnswer serviceOfAnswer) {
        this.serviceOfAnswer = serviceOfAnswer;
    }

    @PostMapping("/")
    public ResponseEntity<?> createAnswer(@RequestBody AnswerDTO answerDTO) {
        try {
            AnswerDTO createdAnswer = serviceOfAnswer.createAnswer(answerDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAnswer);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create answer: " + e.getMessage());
        }
    }

    // Other endpoints for retrieving, updating, and deleting answers

}

 

