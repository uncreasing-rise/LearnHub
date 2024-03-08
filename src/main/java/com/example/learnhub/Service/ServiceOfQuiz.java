package com.example.learnhub.Service;

import com.example.learnhub.DTO.QuestionDTO;
import com.example.learnhub.DTO.QuizDTO;
import com.example.learnhub.Entity.Question;
import com.example.learnhub.Entity.Quiz;
import com.example.learnhub.Entity.Section;
import com.example.learnhub.Repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceOfQuiz {
    private final QuizRepository quizRepository;
    private final ServiceOfQuestion serviceOfQuestion;

    @Autowired
    public ServiceOfQuiz(QuizRepository quizRepository, ServiceOfQuestion serviceOfQuestion) {
        this.quizRepository = quizRepository;
        this.serviceOfQuestion = serviceOfQuestion;
    }

    @Transactional
    public List<Quiz> createQuizzes(Section section, List<QuizDTO> quizDTOs) {
        List<Quiz> quizzes = new ArrayList<>();

        for (QuizDTO quizDTO : quizDTOs) {
            Quiz quiz = convertToQuizEntity(quizDTO, section);
            quizzes.add(quiz);
        }

        return quizRepository.saveAll(quizzes);
    }

    private Quiz convertToQuizEntity(QuizDTO quizDTO, Section section) {
        Quiz quiz = new Quiz();
        quiz.setTitle(quizDTO.getQuizTitle());
        quiz.setSection(section);

        List<QuestionDTO> questionDTOs = quizDTO.getQuestions();
        List<Question> questions = serviceOfQuestion.createQuestions(quiz, questionDTOs);
        quiz.setQuestions(questions);

        return quiz;
    }
}
