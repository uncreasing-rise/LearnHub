package com.example.learnhub.DTO;
import lombok.Data;

import java.util.Date;
@Data
public class RatingDTO {
    private Integer ratingId;
    private Integer ratingValue;
    private Integer courseId;
    private Integer userId;
    private Date ratingTime;
    private Integer ratingCount;

}
