package com.example.learnhub.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "Category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CategoryID")
    private Integer categoryId;

    @Column(name = "CategoryName")
    private String categoryName;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Course> courses;

    // getters and setters
}
