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

    @Column(name = "VideoData")
    private String videoData;

    @Column(name = "VideoScript")
    private String videoScript;

    @Column(name = "isTrial")
    private Boolean isTrial;

    @ManyToOne
    @JoinColumn(name = "SectionID")
    private Section section;

}
