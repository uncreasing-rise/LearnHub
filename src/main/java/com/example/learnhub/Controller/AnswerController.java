package com.example.learnhub.Controller;

import com.example.learnhub.DTO.AnswerDTO;
import com.example.learnhub.Entity.Answer;
import com.example.learnhub.Entity.Question;
import com.example.learnhub.Repository.AnswerRepository;
import com.example.learnhub.Repository.QuestionRepository;
import com.example.learnhub.Service.ServiceOfAnswer;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/answers")
public class AnswerController {

    private final ServiceOfAnswer serviceOfAnswer;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;



    public AnswerController(ServiceOfAnswer serviceOfAnswer, QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.serviceOfAnswer = serviceOfAnswer;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }



    @PutMapping("/{id}")
    public ResponseEntity<?> updateAnswer(@PathVariable("id") Integer id, @RequestBody AnswerDTO answerDTO) {
        try {
            // Retrieve existing Answer from the database
            Answer existingAnswer = answerRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Answer not found with id: " + id));

            // Update the properties of the existing Answer
            existingAnswer.setText(answerDTO.getAnswerText());
            existingAnswer.setCorrect(answerDTO.getIsCorrect());


            // Save the updated Answer
            answerRepository.save(existingAnswer);

            return ResponseEntity.ok("Answer with id " + id + " updated successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Answer not found for id: " + id);
        }
    }



}
