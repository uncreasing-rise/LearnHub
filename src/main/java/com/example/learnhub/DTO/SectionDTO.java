package com.example.learnhub.DTO;

import com.example.learnhub.DTO.ArticleDTO;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Data
public class SectionDTO {
    private Integer sectionId;
    private String sectionName;
    private Integer courseId; // Renamed to follow camelCase convention
    private List<MultipartFile> videoFiles; // List of MultipartFile objects for videos
    private List<MultipartFile> articleFiles;
}
