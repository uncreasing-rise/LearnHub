package com.example.learnhub.Service;

import com.example.learnhub.DTO.AnswerDTO;
import com.example.learnhub.Entity.Answer;
import com.example.learnhub.Entity.Question;

import javax.transaction.Transactional;
import java.util.List;

public interface IServiceOfAnswer {



    @Transactional
    Answer getAnswerById(int id, Question question);


    @org.springframework.transaction.annotation.Transactional
    Answer createAnswer(AnswerDTO answerDTO);
}
