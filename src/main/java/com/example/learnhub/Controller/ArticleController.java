package com.example.learnhub.Controller;

import com.example.learnhub.Entity.Article;
import com.example.learnhub.Entity.Video;
import com.example.learnhub.Service.ServiceOfArticle;
import com.example.learnhub.Service.ServiceOfFile;
import com.example.learnhub.Service.ServiceOfVideo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ServiceOfArticle articleService;
    private final ServiceOfFile fileService; // Inject the ServiceOfFile bean

    @Autowired
    public ArticleController(ServiceOfArticle articleService, ServiceOfFile fileService) {
        this.articleService = articleService;
        this.fileService = fileService; // Initialize the fileService bean
    }

    @PostMapping("/create")
    public ResponseEntity<Article> createVideo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description) {
        // Check if file is provided
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        try {
            fileService.uploadFile(file);

            // Upload video file to Google Cloud Storage
            String gcsPath = "gs://" + "learnhub_bucket" + "/" + file.getOriginalFilename();

            // Create Video entity
            Article article = new Article();
            article.setTitle(title);
            article.setDescription(description);
            article.setArticleData(gcsPath); // Set GCS URI as video data

            // Save the Video entity
            Article createdArticle = articleService.createArticle(article);

            return ResponseEntity.status(HttpStatus.CREATED).body(createdArticle);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Return null or handle the error appropriately
        }
    }
}