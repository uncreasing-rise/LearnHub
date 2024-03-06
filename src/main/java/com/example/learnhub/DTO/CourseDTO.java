package com.example.learnhub.DTO;

import com.example.learnhub.Entity.Category;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class CourseDTO {
    private Integer courseID;
    private String courseTitle;
    private String courseDes;
    private Double coursePrice;
    private Category category;
    private Boolean isPassed;
    private Date courseDate;
    private Integer ratingId;
    private String level;
    private String tag;
    private Integer userId;
    private LearningDetailDTO learningDetail;
    private ImageDTO image;  // Added ImageDTO field
    private Integer status;
    private List<SectionDTO> sections = new ArrayList<>();

    // Constructors, getters, and setters can be generated using Lombok
}
