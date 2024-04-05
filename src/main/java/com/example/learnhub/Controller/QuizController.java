package com.example.learnhub.Controller;

import com.example.learnhub.DTO.QuizDTO;
import com.example.learnhub.DTO.common.enums.ErrorMessage;
import com.example.learnhub.DTO.common.response.ApiResponse;
import com.example.learnhub.DTO.quiz.request.SubmitAnswerRequest;
import com.example.learnhub.DTO.quiz.response.QuizAnswerResponse;
import com.example.learnhub.Entity.Quiz;
import com.example.learnhub.Entity.User;
import com.example.learnhub.Exceptions.BusinessException;
import com.example.learnhub.Service.ServiceOfQuiz;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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

    @GetMapping("/{quizId}")
    public ResponseEntity<QuizDTO> getQuiz(@PathVariable("quizId") Integer quizId) {
        try {
            QuizDTO quizDTO = serviceOfQuiz.getQuizById(quizId);
            if (quizDTO != null) {
                return ResponseEntity.ok(quizDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorMessage.USER_GET_QUIZ_FAILED);
        }
    }


    //-----

    //TODO: submit answer
    @PostMapping("/{quizId}/submit")
    public ResponseEntity<ApiResponse<QuizAnswerResponse>> submitAnswer(Principal principal, @PathVariable(name = "quizId") Integer quizId, @Validated @RequestBody SubmitAnswerRequest request){
        try {
            request.setPrincipal(principal.getName());
            request.setQuizId(quizId);
            ApiResponse<QuizAnswerResponse> response = serviceOfQuiz.submitProcess(request);
            return new ResponseEntity<>(response,HttpStatus.OK);
        } catch (Exception e ) {
            throw new BusinessException(ErrorMessage.USER_SUBMIT_ANSWER_FAILED);
        }
    }
    //TODO: get answer history of quiz
    @GetMapping("/{quizId}/history")
    public ResponseEntity<ApiResponse<List<QuizAnswerResponse>>> getAnswerHistory(Principal principal,@PathVariable("quizId")Integer id) {
        try {
            ApiResponse<List<QuizAnswerResponse>> response = serviceOfQuiz.getHistory(principal, id);
            return new ResponseEntity<>(response,HttpStatus.OK);
        } catch (Exception e ) {
            throw new BusinessException(ErrorMessage.USER_GET_FAIL);
        }
    }

}