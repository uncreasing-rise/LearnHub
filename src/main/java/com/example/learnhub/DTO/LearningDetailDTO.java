package com.example.learnhub.DTO;

import lombok.Data;

@Data
public class LearningDetailDTO {
    private Integer learningDetailId;
    private Integer courseId;
    private String benefit;
    private String objective;
}
