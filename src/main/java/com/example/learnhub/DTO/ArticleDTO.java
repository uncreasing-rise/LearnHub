package com.example.learnhub.DTO;

import com.example.learnhub.Entity.Section;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArticleDTO {
    private Integer articleId;
    private String articleData;
    private Section section;
    private String Title;
}
