package com.example.learnhub.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Article")
public class Article {
    @Id
    @Column(name = "ArticleID")
    private Integer articleId;

    @Column(name = "ArticleData")
    private String articleData;

    @Column(name = "SectionID")
    private Integer sectionId;

}