package com.example.learnhub.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ImageID")
    private Integer imageId;

    @Column(name = "ImageURL")
    private String imageUrl;



    // Constructors, getters, setters, and other methods as needed
}
