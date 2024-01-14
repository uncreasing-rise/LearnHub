package com.example.learnhub.DTO;

import lombok.Data;

@Data
public class VideoDTO {
    private Integer videoId;
    private String videoData;
    private String videoScript;
    private Boolean isTrial;
    private Integer sectionId;

}
