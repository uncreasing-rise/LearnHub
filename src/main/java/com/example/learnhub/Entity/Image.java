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

    @OneToOne(mappedBy = "image")  // Assuming a One-to-One relationship with another entity
    private Course course;  // Adjust the entity name and attribute accordingly

}
