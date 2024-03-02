package com.example.learnhub.Service;

import com.example.learnhub.DTO.QuizDTO;
import com.example.learnhub.Entity.Question;
import com.example.learnhub.Entity.Quiz;
import com.example.learnhub.Repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceOfQuiz {

    private final QuizRepository quizRepository;

    @Autowired
    public ServiceOfQuiz(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    public QuizDTO createQuiz(QuizDTO quizDTO) {
        // Convert DTO to entity
        Quiz quiz = convertToQuizEntity(quizDTO);

        // Validate number of questions
        if (quizDTO.getQuestions().size() < 2) {
            throw new IllegalArgumentException("A quiz must have at least two questions.");
        }

        // Map questions from DTO to entities
        List<Question> questions = quizDTO.getQuestions().stream()
                .map(questionDTO -> {
                    Question question = new Question();
                    question.setText(questionDTO.getQuestionText());
                    // Map other properties as needed
                    return question;
                })
                .collect(Collectors.toList());

        // Set questions to the quiz
        quiz.setQuestions(questions);

        // Save the entity
        Quiz savedQuiz = quizRepository.save(quiz);

        // Convert entity to DTO
        return convertToQuizDTO(savedQuiz);
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
