package com.example.learnhub.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data@AllArgsConstructor
@NoArgsConstructor
public class RatingDTO {
    private Integer ratingId;
    private Integer ratingValue;
    private Integer courseId;
    private Integer userId;
    private Date ratingTime;
    private Integer ratingCount;

}
