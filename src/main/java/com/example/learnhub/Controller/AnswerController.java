package com.example.learnhub.Controller;

import com.example.learnhub.DTO.AnswerDTO;
import com.example.learnhub.Service.ServiceOfAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{id}")
    public ResponseEntity<?> getAnswerById(@PathVariable("id") Integer id) {
        try {
            AnswerDTO answerDTO = serviceOfAnswer.getAnswerById(id);
            return ResponseEntity.ok(answerDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Answer not found for id: " + id);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAnswer(@PathVariable("id") Integer id, @RequestBody AnswerDTO answerDTO) {
        try {
            AnswerDTO updatedAnswer = serviceOfAnswer.updateAnswer(id, answerDTO);
            return ResponseEntity.ok(updatedAnswer);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update answer: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAnswer(@PathVariable("id") Integer id) {
        try {
            serviceOfAnswer.deleteAnswer(id);
            return ResponseEntity.ok("Answer deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Answer not found for id: " + id);
        }
    }
}
