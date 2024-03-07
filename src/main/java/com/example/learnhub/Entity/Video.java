package com.example.learnhub.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Video")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VideoID")
    private Integer videoId;

    @Column(name = "Title")
    private String title;

    @Column(name = "Description")
    private String description;

    @Column(name = "VideoData")
    private String videoData;

    @Column(name = "VideoScript")
    private String videoScript;

    @Column(name = "isTrial")
    private Boolean isTrial;

    @Column(name = "SectionID")
    private Section section;
}
