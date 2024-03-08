package com.example.learnhub.Repository;

import com.example.learnhub.Entity.Answer;
import com.example.learnhub.Entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer> {

    void deleteAllByQuestion(Question question); // Corrected method

    Answer findByIdAndQuestion(int id, Question question);
}
