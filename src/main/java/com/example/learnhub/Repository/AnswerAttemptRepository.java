package com.example.learnhub.Repository;

import com.example.learnhub.Entity.AnswerAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerAttemptRepository extends JpaRepository<AnswerAttempt, Integer> {
}
