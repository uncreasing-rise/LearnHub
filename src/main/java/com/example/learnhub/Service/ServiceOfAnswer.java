package com.example.learnhub.Service;

import com.example.learnhub.DTO.AnswerDTO;
import com.example.learnhub.Entity.Answer;
import com.example.learnhub.Entity.Question;
import com.example.learnhub.Repository.AnswerRepository;
import com.example.learnhub.Repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class ServiceOfAnswer {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Autowired
    public ServiceOfAnswer(QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    @Transactional
    public AnswerDTO createAnswer(AnswerDTO answerDTO) {
        validateAnswerDTO(answerDTO); // Validate the AnswerDTO

        Optional<Question> optionalQuestion = questionRepository.findById(answerDTO.getQuestionId());
        if (optionalQuestion.isPresent()) {
            Question question = optionalQuestion.get();
            Answer answer = mapAnswerDTOToEntity(answerDTO, question); // Pass the found question
            Answer savedAnswer = answerRepository.save(answer);
            return mapAnswerEntityToDTO(savedAnswer);
        } else {
            throw new RuntimeException("Question not found");
        }
    }

    private void validateAnswerDTO(AnswerDTO answerDTO) {
        if (answerDTO.getQuestionId() == null) {
            throw new IllegalArgumentException("Question ID cannot be null");
        }
        // Add more validation if needed
    }

    private Answer mapAnswerDTOToEntity(AnswerDTO answerDTO, Question question) {
        Answer answer = new Answer();
        answer.setQuestion(question);
        answer.setText(answerDTO.getAnswerText());
        answer.setCorrect(answerDTO.getIsCorrect());
        return answer;
    }

    private AnswerDTO mapAnswerEntityToDTO(Answer answer) {
        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.setAnswerId(answer.getId());
        answerDTO.setQuestionId(answer.getQuestion().getId());
        answerDTO.setAnswerText(answer.getText());
        answerDTO.setIsCorrect(answer.isCorrect());
        return answerDTO;
    }
}
