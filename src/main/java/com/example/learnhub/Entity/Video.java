package com.example.learnhub.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @Column(name = "video_data")
    private String videoData;

    @Column(name = "is_trial")
    private Boolean isTrial;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "SectionID") // Many-to-One association with Section
    private Section section;
}
