package com.example.learnhub.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Video")
public class Video {
    @Id
    @Column(name = "VideoID")
    private Integer videoId;

    @Column(name = "VideoData")
    private String videoData;

    @Column(name = "VideoScript")
    private String videoScript;

    @Column(name = "isTrial")
    private Boolean isTrial;

    @Column(name = "SectionID")
    private Integer sectionId;

}
