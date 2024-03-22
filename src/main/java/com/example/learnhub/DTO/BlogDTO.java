package com.example.learnhub.DTO;

import com.example.learnhub.Entity.Category;
import lombok.*;

import java.time.LocalDateTime;
@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogDTO {
    private Integer id;
    private String title;
    private String description;
    private Category category;
    private LocalDateTime createdDate;
    private String CategoryName;
    private String ImageUrl;
}
