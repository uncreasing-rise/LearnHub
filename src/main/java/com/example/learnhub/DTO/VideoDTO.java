package com.example.learnhub.DTO;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class VideoDTO {
    private Integer videoId;
    private String videoData;
    private String videoScript;
    private Boolean isTrial;
    private Integer sectionID;
    private MultipartFile videoFile;
}
