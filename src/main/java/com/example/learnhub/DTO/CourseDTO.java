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
    private LearningDetailDTO learningDetail;
    private String image;
    private Integer status;
    private List<SectionDTO> sections = new ArrayList<>();
    private double avgRating;
    private int countRating;
    private List<ResponeSectionDTO> sections1 = new ArrayList<>();
    private String Video;
}
