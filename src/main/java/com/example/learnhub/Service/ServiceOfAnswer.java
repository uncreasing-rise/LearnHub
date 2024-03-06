package com.example.learnhub.Service;

import com.example.learnhub.DTO.AnswerDTO;
import com.example.learnhub.Entity.Answer;
import com.example.learnhub.Entity.Question;
import com.example.learnhub.Repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServiceOfAnswer implements IServiceOfAnswer {

    private final AnswerRepository answerRepository;

    @Autowired
    public ServiceOfAnswer(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    // Existing methods (e.g., updateAnswer, deleteAnswer, getAnswerById, getAllAnswers)

    @Override
    public Answer getAnswerById(int id, Question question) {
        return null;
    }

    @Override
    @Transactional
    public Answer createAnswer(AnswerDTO answerDTO) {
        Answer answer = convertToAnswerEntity(answerDTO);
        return answerRepository.save(answer);
    }

    private Answer convertToAnswerEntity(AnswerDTO answerDTO) {
        // Implement logic to map answerDTO properties to an Answer object
        Answer answer = new Answer();
        answer.setText(answerDTO.getAnswerText());
        answer.setCorrect(answerDTO.getIsCorrect());
        answer.setQuestion(answerDTO.getQuestion());        return answer;
    }
}
