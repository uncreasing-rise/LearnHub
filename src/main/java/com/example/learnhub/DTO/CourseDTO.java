package com.example.learnhub.DTO;

import lombok.Data;

import java.util.Date;
@Data
public class CourseDTO {
    private Integer courseId;
    private String courseTitle;
    private String courseDes;
    private Double coursePrice;
    private Integer categoryId;
    private Boolean isPassed;
    private Date courseDate;
    private Integer ratingId;
    private String level;
    private String tag;
    private Integer userId;

}
