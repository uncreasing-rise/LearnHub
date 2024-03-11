package com.example.learnhub.Service;

import com.example.learnhub.DTO.QuestionDTO;
import com.example.learnhub.DTO.QuizDTO;
import com.example.learnhub.Entity.Question;
import com.example.learnhub.Entity.Quiz;
import com.example.learnhub.Entity.Section;
import com.example.learnhub.Repository.QuizRepository;
import com.example.learnhub.Repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceOfQuiz {
    private final QuizRepository quizRepository;
    private final ServiceOfQuestion serviceOfQuestion;
    private final SectionRepository sectionRepository;

    @Autowired
    public ServiceOfQuiz(QuizRepository quizRepository, ServiceOfQuestion serviceOfQuestion, SectionRepository sectionRepository) {
        this.quizRepository = quizRepository;
        this.serviceOfQuestion = serviceOfQuestion;
        this.sectionRepository = sectionRepository;
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

    @Transactional
    public void updateQuiz(QuizDTO quizDTO) {
        try {
            Optional<Quiz> optionalQuiz = quizRepository.findById(quizDTO.getQuizId());
            if (optionalQuiz.isPresent()) {
                Quiz existingQuiz = optionalQuiz.get();
                // Update quiz properties
                existingQuiz.setTitle(quizDTO.getQuizTitle());
                // Update other properties if needed
                // Save the updated quiz
                quizRepository.save(existingQuiz);
            } else {
                throw new IllegalArgumentException("Quiz not found for ID: " + quizDTO.getQuizId());
            }
        } catch (Exception e) {
            // Handle update failure
            e.printStackTrace();
        }
    }


    @Transactional
    public Quiz createQuizToSection(Integer sectionId, QuizDTO quizDTO) {
        try {
            Optional<Section> optionalSection = sectionRepository.findById(sectionId);
            if (optionalSection.isPresent()) {
                Section section = optionalSection.get();
                Quiz quiz = convertToQuizEntity(quizDTO, section);
                return quizRepository.save(quiz);
            } else {
                throw new IllegalArgumentException("Section not found for ID: " + sectionId);
            }
        } catch (Exception e) {
            // Handle creation failure
            e.printStackTrace();
            return null;
        }
    }
    @Transactional
    public void deleteQuizFromSection(Integer sectionId, Integer quizId) {
        try {
            Optional<Section> optionalSection = sectionRepository.findById(sectionId);
            if (optionalSection.isPresent()) {
                Section section = optionalSection.get();
                Optional<Quiz> optionalQuiz = quizRepository.findById(quizId);
                if (optionalQuiz.isPresent()) {
                    Quiz quiz = optionalQuiz.get();
                    // Remove the quiz from the section
                    section.getQuizzes().remove(quiz);
                    sectionRepository.save(section);
                    // Delete the quiz
                    quizRepository.delete(quiz);
                } else {
                    throw new IllegalArgumentException("Quiz not found for ID: " + quizId);
                }
            } else {
                throw new IllegalArgumentException("Section not found for ID: " + sectionId);
            }
        } catch (Exception e) {
            // Handle deletion failure
            e.printStackTrace();
        }
    }

}
