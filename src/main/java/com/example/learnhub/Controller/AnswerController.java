package com.example.learnhub.Controller;

import com.example.learnhub.DTO.AnswerDTO;
import com.example.learnhub.Entity.Answer;
import com.example.learnhub.Entity.Question;
import com.example.learnhub.Repository.QuestionRepository;
import com.example.learnhub.Service.ServiceOfAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/answers")
public class AnswerController {

    private final ServiceOfAnswer serviceOfAnswer;
    private final QuestionRepository questionRepository;

    @Autowired
    public AnswerController(ServiceOfAnswer serviceOfAnswer, QuestionRepository questionRepository) {
        this.serviceOfAnswer = serviceOfAnswer;
        this.questionRepository = questionRepository;
    }


    @PostMapping("/create")
    public ResponseEntity<Answer> createAnswerToQuestion(@RequestParam("questionId") Integer questionId, @RequestBody AnswerDTO answerDTO) {
        try {
            Answer createdAnswer = serviceOfAnswer.createAnswerToQuestion(questionId, answerDTO);
            return ResponseEntity.ok(createdAnswer);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update/{questionId}")
    public ResponseEntity<Question> updateAnswer(
            @PathVariable("questionId") Integer questionId,
            @RequestBody AnswerDTO answerDTO
    ) {
        // Retrieve the existing question by ID
        Question existingQuestion = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Question not found for ID: " + questionId));

        // Update the answer
        serviceOfAnswer.updateAnswer(existingQuestion, answerDTO);

        return ResponseEntity.ok(existingQuestion);
    }
    @DeleteMapping("/{questionId}/delete/{answerId}")
    public ResponseEntity<String> deleteAnswerFromQuestion(@PathVariable("questionId") Integer questionId, @PathVariable("answerId") Integer answerId) {
        boolean isDeleted = serviceOfAnswer.deleteAnswerFromQuestion(questionId, answerId);
        if (isDeleted) {
            return ResponseEntity.ok("Answer deleted successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to delete answer from question.");
        }
    }
}
