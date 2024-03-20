package com.example.learnhub.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Setter
@Getter
@Data
public class BlogDTO {
    private Integer id;
    private String title;
    private String description;
    private Integer categoryId;
    private LocalDateTime createdDate;

}
