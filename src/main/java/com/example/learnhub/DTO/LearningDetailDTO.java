package com.example.learnhub.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@AllArgsConstructor
@NoArgsConstructor
public class LearningDetailDTO {
    private Integer learningDetailId;
    private Integer courseId;
    private String benefit;
    private String objective;
}
