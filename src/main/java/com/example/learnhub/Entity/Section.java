package com.example.learnhub.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

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

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL)
    private List<Video> videos;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL)
    private List<Article> articles;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL)
    private List<Quiz> quizzes;

    @ManyToOne
    @JoinColumn(name = "CourseID")
    private Course course;
}
