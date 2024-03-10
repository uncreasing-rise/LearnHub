package com.example.learnhub.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @Column(name = "article_data")
    private String articleUrl; // Change type to String to store URL

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "SectionID") // Many-to-One association with Section
    private Section section;
}
