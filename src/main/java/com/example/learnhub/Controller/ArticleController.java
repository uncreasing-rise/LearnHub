package com.example.learnhub.Controller;

import com.example.learnhub.DTO.ArticleDTO;
import com.example.learnhub.Entity.Article;
import com.example.learnhub.Service.ServiceOfArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ServiceOfArticle serviceOfArticle;

    @Autowired
    public ArticleController(ServiceOfArticle serviceOfArticle) {
        this.serviceOfArticle = serviceOfArticle;
    }

    @PutMapping("/update/{articleId}/file")
    public ResponseEntity<Article> updateArticleFile(
            @PathVariable("articleId") Integer articleId,
            @RequestPart("articleFile") MultipartFile articleFile
    ) {
        try {
            Article updatedArticle = serviceOfArticle.updateArticleFile(articleId, articleFile);
            return ResponseEntity.ok(updatedArticle);
        } catch (IllegalArgumentException e) {
            // Handle invalid input or article not found
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update/{articleId}/content")
    public ResponseEntity<Article> updateArticleContent(
            @PathVariable("articleId") Integer articleId,
            @RequestBody ArticleDTO articleDTO
    ) {
        try {
            Article updatedArticle = serviceOfArticle.updateArticleContent(articleId, articleDTO);
            return ResponseEntity.ok(updatedArticle);
        } catch (IllegalArgumentException e) {
            // Handle invalid input or article not found
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



    @PostMapping("/{sectionId}")
    public ResponseEntity<Article> createArticleToSection(
            @PathVariable("sectionId") Integer sectionId,
            @RequestParam("articleFile") MultipartFile articleFile,
            @RequestBody ArticleDTO articleDTO
    ) {
        try {
            Article createdArticle = serviceOfArticle.createArticleToSection(sectionId, articleFile, articleDTO);
            return ResponseEntity.ok(createdArticle);
        } catch (IllegalArgumentException e) {
            // Handle invalid input or section not found
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (RuntimeException e) {
            // Handle runtime exceptions (e.g., file upload failure)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @DeleteMapping("/{sectionId}/delete/{articleId}")
    public ResponseEntity<Void> deleteArticleFromSection(
            @PathVariable("sectionId") Integer sectionId,
            @PathVariable("articleId") Integer articleId
    ) {
        boolean deleted = serviceOfArticle.deleteArticleFromSection(sectionId, articleId);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
