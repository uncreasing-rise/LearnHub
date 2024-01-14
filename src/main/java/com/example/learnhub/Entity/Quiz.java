package com.example.learnhub.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Quiz")
public class Quiz {
    @Id
    @Column(name = "QuizID")
    private Integer quizId;

    @Column(name = "CourseID")
    private Integer courseId;

    @Column(name = "QuizTitle")
    private String quizTitle;

    @Column(name = "QuizDescription")
    private String quizDescription;

}
