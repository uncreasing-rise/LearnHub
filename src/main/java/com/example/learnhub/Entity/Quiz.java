package com.example.learnhub.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Quiz")
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "QuizID")
    private Integer quizId;

    @ManyToOne
    @JoinColumn(name = "CourseID")
    private Course course;

    @Column(name = "QuizTitle")
    private String quizTitle;

    @Column(name = "QuizDescription")
    private String quizDescription;

    // Constructors, getters, setters, and other methods as needed
}
