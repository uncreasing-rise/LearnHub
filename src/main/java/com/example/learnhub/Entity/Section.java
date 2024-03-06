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

    @Column(name = "VideoPath")
    private String videoPath;

    @Column(name = "ArticlePath")
    private String articlePath;

    @ManyToOne
    @JoinColumn(name = "CourseID")
    private Course course;
}
