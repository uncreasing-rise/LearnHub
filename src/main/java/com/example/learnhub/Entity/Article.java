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
    private Integer articleId;

    @Column(name = "ArticleData")
    private String articleData;

    @ManyToOne
    @JoinColumn(name = "SectionID", nullable = false)
    private Section section;

}
