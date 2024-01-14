package com.example.learnhub.DTO;
import lombok.Data;

import java.util.Date;
@Data
public class RatingDTO {
    private Integer ratingId;
    private Double ratingValue;
    private Integer courseId;
    private Integer userId;
    private Date ratingTime;

}
