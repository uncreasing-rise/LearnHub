package com.example.learnhub.Controller;

import com.example.learnhub.DTO.QuestionDTO;
import com.example.learnhub.Entity.Question;
import com.example.learnhub.Entity.Quiz;
import com.example.learnhub.Repository.QuizRepository;
import com.example.learnhub.Service.ServiceOfQuestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final ServiceOfQuestion serviceOfQuestion;
    private final QuizRepository quizRepository;

    @Autowired
    public QuestionController(ServiceOfQuestion serviceOfQuestion, QuizRepository quizRepository) {
        this.serviceOfQuestion = serviceOfQuestion;
        this.quizRepository = quizRepository;
    }

    @PutMapping("/update/{quizId}")
    public ResponseEntity<Quiz> updateQuestions(
            @PathVariable("quizId") Integer quizId,
            @RequestBody List<QuestionDTO> questionDTOs
    ) {
        // Retrieve the existing quiz by ID
        Quiz existingQuiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Quiz not found for ID: " + quizId));

        // Update the questions
        serviceOfQuestion.updateQuestion(existingQuiz, questionDTOs);

        return ResponseEntity.ok(existingQuiz);
    }
    @PostMapping("/{quizId}/questions")
    public ResponseEntity<Question> createQuestionToQuiz(@PathVariable("quizId") Integer quizId, @RequestBody QuestionDTO questionDTO) {
        Question createdQuestion = serviceOfQuestion.createQuestionToQuiz(quizId, questionDTO);
        if (createdQuestion != null) {
            return ResponseEntity.ok(createdQuestion);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping("/{quizId}/questions/{questionId}")
    public ResponseEntity<Void> deleteQuestionFromQuiz(
            @PathVariable("quizId") Integer quizId,
            @PathVariable("questionId") Integer questionId
    ) {
        // Attempt to delete the question from the quiz
        boolean deleted = serviceOfQuestion.deleteQuestionFromQuiz(quizId, questionId);

        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
