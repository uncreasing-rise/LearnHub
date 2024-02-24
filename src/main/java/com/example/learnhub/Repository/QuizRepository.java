package com.example.learnhub.Repository;

import com.example.learnhub.Entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Integer> {
}
