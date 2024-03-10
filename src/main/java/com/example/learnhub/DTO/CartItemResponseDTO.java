package com.example.learnhub.DTO;

import com.example.learnhub.Entity.CartItem;
import com.example.learnhub.Entity.Image;
import com.example.learnhub.Entity.Rating;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Data
public class CartItemResponseDTO {
    private Integer cartItemId;
    private Integer courseId;
    private String courseTitle;
    private String courseDes;
    private Double coursePrice;
    private String categoryName;
    private Boolean isPassed;
    private Date courseDate;
    private String level;
    private String tag;
    private Integer userId;
    private List<Image> images;
    private Integer status = 0;


}
