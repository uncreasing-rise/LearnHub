package com.example.learnhub.Service;

import com.example.learnhub.DTO.AnswerDTO;
import com.example.learnhub.DTO.QuestionDTO;
import com.example.learnhub.Entity.Answer;
import com.example.learnhub.Entity.Question;
import com.example.learnhub.Repository.QuestionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceOfQuestion implements IServiceOfQuestion {
    private final QuestionRepository questionRepository;
    private final ServiceOfAnswer serviceOfAnswer;

    public ServiceOfQuestion(QuestionRepository questionRepository, ServiceOfAnswer serviceOfAnswer) {
        this.questionRepository = questionRepository;
        this.serviceOfAnswer = serviceOfAnswer;
    }

    @Transactional
    public Question createQuestion(QuestionDTO questionDTO) {
        // Convert DTO to entity
        Question question = convertToQuizEntity(questionDTO);

        // Validate number of answers
        if (questionDTO.getAnswerDTOs().size() < 2) {
            throw new IllegalArgumentException("A question must have at least two answers.");
        }

        // Save the question entity first to obtain its ID
        Question savedQuestion = questionRepository.save(question);

        // Map answers from DTO to entities using ServiceOfAnswer
        List<Answer> answers = new ArrayList<>();
        for (AnswerDTO answerDTO : questionDTO.getAnswerDTOs()) {
            Answer answer = serviceOfAnswer.createAnswer(answerDTO);
            // Set the question for the answer
            answer.setQuestion(savedQuestion);
            answers.add(answer);
        }

        // Set answers to the question
        savedQuestion.setAnswers(answers);

        // Check if the question has at least one answer
        if (answers.isEmpty()) {
            throw new IllegalArgumentException("A question must have at least one answer.");
        }

        // Return the saved question entity
        return savedQuestion;
    }

    private Question convertToQuizEntity(QuestionDTO questionDTO) {
        Question question = new Question();
        question.setText(questionDTO.getQuestionText());
        question.setPoint(questionDTO.getPoint());
        // Set other properties as needed
        return question;
    }

}
