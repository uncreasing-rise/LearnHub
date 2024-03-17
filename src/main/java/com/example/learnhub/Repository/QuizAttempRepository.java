package com.example.learnhub.Repository;

import com.example.learnhub.Entity.Quiz;
import com.example.learnhub.Entity.QuizAttempt;
import com.example.learnhub.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizAttempRepository extends JpaRepository<QuizAttempt, Integer> {

    List<QuizAttempt> findByUserAndQuiz(User user , Quiz quiz);
}
