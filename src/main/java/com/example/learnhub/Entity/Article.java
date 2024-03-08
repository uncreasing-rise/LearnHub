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

    @Column(name = "ArticleData")
    private String articleUrl; // Change type to String to store URL

    @ManyToOne
    @JoinColumn(name = "SectionID") // Many-to-One association with Section
    private Section section;
}
