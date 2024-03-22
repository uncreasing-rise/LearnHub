package com.example.learnhub.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WishlistDTO {
    private Integer wishlistId;
    private Integer userId;
    private Integer courseId;
    private String courseImage;
    private String courseTitle;
    private String courseCategory;
}
