package com.example.learnhub.DTO;

import lombok.Data;

@Data
public class WishlistDTO {
    private Integer wishlistId;
    private Integer userId;
    private Integer courseId;
    private Integer rating; // Add rating field
}
