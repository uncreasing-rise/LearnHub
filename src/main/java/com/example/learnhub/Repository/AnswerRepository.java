package com.example.learnhub.Repository;

import com.example.learnhub.Entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Integer> {
    // Define custom query methods if needed
}
