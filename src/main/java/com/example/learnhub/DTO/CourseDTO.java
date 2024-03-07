package com.example.learnhub.DTO;

import com.example.learnhub.Entity.Category;
import com.example.learnhub.Entity.LearningDetail;
import com.example.learnhub.Entity.Rating;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
    private List<Rating> ratings;
    private String level;
    private String tag;
    private Integer userId;
    @Setter
    private LearningDetailDTO learningDetail;
    private ImageDTO image;  // Added ImageDTO field
    private Integer status;
    private List<SectionDTO> sections = new ArrayList<>();
    private double avgRating;
    // Constructors, getters, and setters can be generated using Lombok
    private int countRating;

}
