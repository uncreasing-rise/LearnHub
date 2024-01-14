package com.example.learnhub.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Image")
public class Image {
    @Id
    @Column(name = "ImageID")
    private Integer imageId;

    @Column(name = "ImageURL")
    private String imageUrl;


}
