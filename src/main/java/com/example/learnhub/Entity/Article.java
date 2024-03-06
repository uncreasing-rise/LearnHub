package com.example.learnhub.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Article")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ArticleID")
    private Integer articleID;

    @Column(name = "Title")
    private String title;

    @Column(name = "Description")
    private String description;

    @Column(name = "ArticleData")
    private String articleData;

    @Column(name = "ArticleScript")
    private String articleScript;

    @Column(name = "isTrial")
    private Boolean isTrial;

    @Column(name = "SectionID")
    private Integer sectionID;
}
