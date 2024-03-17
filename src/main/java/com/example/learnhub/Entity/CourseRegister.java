package com.example.learnhub.Entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "course_register")
public class CourseRegister {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @OneToOne
    private Course course;

    @OneToOne
    private User user;

    private Integer orderId;
    private Integer paymentId;
}
