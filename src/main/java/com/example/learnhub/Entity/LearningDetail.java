package com.example.learnhub.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "learning_detail")
public class LearningDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "learning_detailid")
    private Integer learningDetailId;


    @OneToOne
    @JoinColumn(name = "courseid") // Assuming you have a column named "course_id" in the LearningDetail table
    private Course course;



    @Column(name = "Benefit")
    private String benefit;

    @Column(name = "Objective")
    private String objective;

}
