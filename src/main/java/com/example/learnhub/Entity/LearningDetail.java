package com.example.learnhub.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "LearningDetail")
public class LearningDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LearningDetailID")
    private Integer learningDetailId;

    @ManyToOne
    @JoinColumn(name = "CourseID")
    private Course course;

    @Column(name = "Benefit")
    private String benefit;

    @Column(name = "Objective")
    private String objective;

}
