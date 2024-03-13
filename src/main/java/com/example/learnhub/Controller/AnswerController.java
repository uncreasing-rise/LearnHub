package com.example.learnhub.Controller;

import com.example.learnhub.DTO.AnswerDTO;
import com.example.learnhub.Entity.Answer;
import com.example.learnhub.Entity.Question;
import com.example.learnhub.Repository.AnswerRepository;
import com.example.learnhub.Repository.QuestionRepository;
import com.example.learnhub.Service.ServiceOfAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin("*")

@RestController
@RequestMapping("/api/answers")
public class AnswerController {

    private final ServiceOfAnswer serviceOfAnswer;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    @Autowired
    public AnswerController(ServiceOfAnswer serviceOfAnswer, AnswerRepository answerRepository, QuestionRepository questionRepository) {
        this.serviceOfAnswer = serviceOfAnswer;
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
    }


    @PostMapping("/create")
    public ResponseEntity<Answer> createAnswerToQuestion(@RequestParam("questionId") Integer questionId, @RequestPart AnswerDTO answerDTO) {
        try {
            Answer createdAnswer = serviceOfAnswer.createAnswerToQuestion(questionId, answerDTO);
            return ResponseEntity.ok(createdAnswer);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update/{answerId}")
    public ResponseEntity<Answer> updateAnswer(
            @PathVariable("answerId") Integer answerId,
            @RequestBody AnswerDTO answerDTO
    ) {
        try {
            // Retrieve the existing answer by ID
            Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
            if (optionalAnswer.isPresent()) {
                Answer existingAnswer = optionalAnswer.get();

                // Update the answer properties with the values from the DTO
                existingAnswer.setText(answerDTO.getAnswerText());
                existingAnswer.setCorrect(answerDTO.getIsCorrect());
                // Assuming the question ID is provided in the DTO

                // Save the updated answer
                Answer updatedAnswer = answerRepository.save(existingAnswer);
                return ResponseEntity.ok(updatedAnswer);
            } else {
                throw new IllegalArgumentException("Answer not found for ID: " + answerId);
            }
        } catch (IllegalArgumentException e) {
            // Handle invalid input or answer not found
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
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
