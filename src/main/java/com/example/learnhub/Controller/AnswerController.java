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

    @PostMapping("/")
    public ResponseEntity<?> createAnswer(@RequestBody AnswerDTO answerDTO, @RequestParam Integer questionId) {
        try {
            // Fetch the question from the database using the provided questionId
            Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new EntityNotFoundException("Question not found for id: " + questionId));

            // Add the answer, providing the answerDTO and the associated question
            serviceOfAnswer.createAnswer(answerDTO);

            return ResponseEntity.status(HttpStatus.CREATED).body("Answer created successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Question not found for id: " + questionId);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create answer: " + e.getMessage());
        }
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

            // If the association between Answer and Question needs to be updated,
            // retrieve the Question object from the database and set it to the existingAnswer
            // For now, I'll assume the association is not being updated, and you don't need to pass a Question object

            // Save the updated Answer
            answerRepository.save(existingAnswer);

            return ResponseEntity.ok("Answer with id " + id + " updated successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Answer not found for id: " + id);
        }
    }


//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteAnswer(@PathVariable("id") Integer id) {
//        try {
//            serviceOfAnswer.deleteAnswer(id); // Corrected method call
//            return ResponseEntity.ok("Answer with id " + id + " deleted successfully");
//        } catch (EntityNotFoundException e) { // Changed exception type to EntityNotFoundException
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Answer not found for id: " + id);
//        }
//    }

}
