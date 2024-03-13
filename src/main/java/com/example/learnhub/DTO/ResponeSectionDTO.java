package com.example.learnhub.DTO;

import com.example.learnhub.Entity.Article;
import com.example.learnhub.Entity.Course;
import com.example.learnhub.Entity.Quiz;
import com.example.learnhub.Entity.Video;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponeSectionDTO {
    private Integer sectionId;
    private String sectionName;
    private Course course;
    private List<Article> articles; // List of URLs for videos
    private List<Video> videos; // List of URLs for articles
    private List<Quiz> quizzes; // List of URLs for articles

}
