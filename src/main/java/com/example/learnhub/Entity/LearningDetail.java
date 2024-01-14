package com.example.learnhub.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "LearningDetail")
public class LearningDetail {
    @Id
    @Column(name = "LearningDetailID")
    private Integer learningDetailId;

    @Column(name = "CourseID")
    private Integer courseId;

    @Column(name = "Benefit")
    private String benefit;

    @Column(name = "Objective")
    private String objective;

}
