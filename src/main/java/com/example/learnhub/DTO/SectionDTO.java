    package com.example.learnhub.DTO;

    import com.example.learnhub.Entity.Article;
    import com.example.learnhub.Entity.Course;
    import com.example.learnhub.Entity.Video;
    import lombok.Data;
    import org.springframework.web.multipart.MultipartFile;
    import java.util.List;

    @Data
    public class SectionDTO {
        private Integer sectionId;
        private String sectionName;
        private Course course;
        private List<ArticleDTO> articles; // List of URLs for videos
        private List<VideoDTO> videos; // List of URLs for articles

        private List<QuizDTO> quizzes; // List of URLs for articles

    }
