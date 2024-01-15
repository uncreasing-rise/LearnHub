package com.example.learnhub.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Section")
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SectionID")
    private Integer sectionId;

    @Column(name = "SectionName")
    private String sectionName;

    @ManyToOne
    @JoinColumn(name = "CourseID")
    private Course course;

}
