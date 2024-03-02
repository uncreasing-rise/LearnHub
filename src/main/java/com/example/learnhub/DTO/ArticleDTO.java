package com.example.learnhub.DTO;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ArticleDTO {
    private Integer articleId;
    private String articleData;
    private Integer sectionId;
    private String articleTitle;
    private MultipartFile articleFile; // New field to store article file
}
