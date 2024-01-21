package com.example.learnhub.DTO;

import com.example.learnhub.Entity.Course;
import lombok.Data;

import java.util.List;

@Data
public class SectionDTO {
    private Integer sectionId;
    private String sectionName;
    private Integer courseID;
    private List<VideoDTO> videos;
    private List<ArticleDTO> articles;
}
