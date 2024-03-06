package com.example.learnhub.Service;

import com.example.learnhub.DTO.QuestionDTO;
import com.example.learnhub.DTO.QuizDTO;
import com.example.learnhub.Entity.Question;
import com.example.learnhub.Entity.Quiz;
import com.example.learnhub.Repository.QuestionRepository;
import com.example.learnhub.Repository.QuizRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceOfQuiz {
    private final QuestionRepository questionRepository;

    private final QuizRepository quizRepository;
    private final ServiceOfQuestion serviceOfQuestion;

    @Autowired
    public ServiceOfQuiz(QuestionRepository questionRepository, QuizRepository quizRepository, ServiceOfQuestion serviceOfQuestion) {
        this.questionRepository = questionRepository;
        this.quizRepository = quizRepository;
        this.serviceOfQuestion = serviceOfQuestion;
    }

    @Transactional
    public Quiz createQuiz(QuizDTO quizDTO) {
        // Convert DTO to entity
        Quiz quiz = convertToQuizEntity(quizDTO);

        // Validate number of questions
        if (quizDTO.getQuestions().size() < 2) {
            throw new IllegalArgumentException("A quiz must have at least two questions.");
        }

        // Save the entity first
        Quiz savedQuiz = quizRepository.save(quiz);

        // Map questions from DTO to entities
        List<Question> questions = new ArrayList<>();
        for (QuestionDTO questionDTO : quizDTO.getQuestions()) {
            // Convert QuestionDTO to Question entity
            Question question = serviceOfQuestion.createQuestion(questionDTO);
            // Set quiz for the question
            question.setQuiz(savedQuiz);
            // Save the question entity
            questionRepository.save(question); // Assuming you have a method to save a single question
            questions.add(question);
        }

        // Set questions to the quiz
        savedQuiz.setQuestions(questions);

        // Return the saved quiz entity
        return savedQuiz;
    }


    private Quiz convertToQuizEntity(QuizDTO quizDTO) {
        Quiz quiz = new Quiz();
        quiz.setTitle(quizDTO.getQuizTitle());
        // Set other properties as needed
        return quiz;
    }

    private QuizDTO convertToQuizDTO(Quiz quiz) {
        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setQuizTitle(quiz.getTitle());
        // Set other properties as needed
        return quizDTO;
    }
}
