package com.example.learnhub.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @Column(name = "section_name")
    private String sectionName;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL)
    private List<Video> videos;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL)

    private List<Article> articles;
    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL)
    private List<Quiz> quizzes;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "CourseID")
    private Course course;
}
