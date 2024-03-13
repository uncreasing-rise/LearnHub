package com.example.learnhub.Controller;

import com.example.learnhub.DTO.QuizDTO;
import com.example.learnhub.Entity.Quiz;
import com.example.learnhub.Service.ServiceOfQuiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin("*")

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final ServiceOfQuiz serviceOfQuiz;

    @Autowired
    public QuizController( ServiceOfQuiz serviceOfQuiz) {
        this.serviceOfQuiz = serviceOfQuiz;
    }

    @PutMapping("/update/{quizId}")
    public ResponseEntity<Void> updateQuiz(
            @PathVariable("quizId") Integer quizId,
            @RequestBody QuizDTO quizDTO
    ) {
        try {
            quizDTO.setQuizId(quizId); // Set the ID in the DTO
            serviceOfQuiz.updateQuiz(quizDTO);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            // Handle invalid input or quiz not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @PostMapping("/{sectionId}/quizzes")
    public ResponseEntity<Quiz> createQuizToSection(@PathVariable("sectionId") Integer sectionId, @RequestBody QuizDTO quizDTO) {
        Quiz createdQuiz = serviceOfQuiz.createQuizToSection(sectionId, quizDTO);
        if (createdQuiz != null) {
            return ResponseEntity.ok(createdQuiz);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{sectionId}/quizzes/{quizId}")
    public ResponseEntity<Void> deleteQuizFromSection(@PathVariable("sectionId") Integer sectionId, @PathVariable("quizId") Integer quizId) {
        try {
            serviceOfQuiz.deleteQuizFromSection(sectionId, quizId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            // Handle invalid input or quiz not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
