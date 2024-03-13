package com.example.learnhub.Service;

import com.example.learnhub.DTO.AnswerDTO;
import com.example.learnhub.Entity.Answer;
import com.example.learnhub.Entity.Question;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IServiceOfAnswer {


    Answer getAnswerById(int id, Question question);
}
