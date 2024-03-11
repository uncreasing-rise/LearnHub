package com.example.learnhub.DTO;

import com.example.learnhub.Entity.Category;
import com.example.learnhub.Entity.LearningDetail;
import com.example.learnhub.Entity.Rating;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponeCourseDTO {
    private Integer courseID;
    private String courseTitle;
    private String courseDes;
    private Double coursePrice;
    private int categoryId;
    private Boolean isPassed;
    private Date courseDate;
    private List<Rating> ratings;
    private String level;
    private String tag;
    private Integer userId;
    private LearningDetailDTO learningDetail;
    private ImageDTO image;  // Added ImageDTO field
    private Integer status;
    private List<ResponeSectionDTO> sections = new ArrayList<>();
    private double avgRating;
    private int countRating;

}
