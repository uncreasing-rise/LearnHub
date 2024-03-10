package com.example.learnhub.Service;

import com.example.learnhub.DTO.AnswerDTO;
import com.example.learnhub.Entity.Answer;
import com.example.learnhub.Entity.Question;
import com.example.learnhub.Repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Service
public class ServiceOfAnswer implements IServiceOfAnswer {
    private final AnswerRepository answerRepository;

    @Autowired
    public ServiceOfAnswer(AnswerRepository answerRepository) {
        this.answerRepository = answerRepository;
    }

    @Override
    public Answer getAnswerById(int id, Question question) {
        return answerRepository.findByIdAndQuestion(id, question);
    }

    @Transactional
    public List<Answer> convertToAnswerEntities(List<AnswerDTO> answerDTOS, Question question) {
        List<Answer> answers = new ArrayList<>();

        for (AnswerDTO answerDTO : answerDTOS) {
            Answer answer = new Answer();
            answer.setText(answerDTO.getAnswerText());
            answer.setCorrect(answerDTO.getIsCorrect());
            answer.setQuestion(question);
            answers.add(answer);
        }

        return answerRepository.saveAll(answers);
    }
}