package com.example.learnhub.Controller;

import com.example.learnhub.DTO.ArticleDTO;
import com.example.learnhub.Entity.Article;
import com.example.learnhub.Entity.Section;
import com.example.learnhub.Service.ServiceOfArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ServiceOfArticle serviceOfArticle;

    @Autowired
    public ArticleController(ServiceOfArticle serviceOfArticle) {
        this.serviceOfArticle = serviceOfArticle;
    }

    @PostMapping("/create")
    public ResponseEntity<Article> createArticle(@RequestBody Article article) {
        Article createdArticle = serviceOfArticle.createArticle(article);
        return new ResponseEntity<>(createdArticle, HttpStatus.CREATED);
    }

    @PostMapping("/create/multiple")
    public ResponseEntity<String> createMultipleArticles(@RequestBody List<ArticleDTO> articleDTOs, @RequestParam Integer sectionId) {
        Section section = new Section();
        section.setSectionId(sectionId);
        serviceOfArticle.createArticles(section, articleDTOs);
        return new ResponseEntity<>("Articles created successfully", HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Integer id, @RequestBody Article updatedArticle) {
        Article article = serviceOfArticle.updateArticle(id, updatedArticle);
        return new ResponseEntity<>(article, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteArticle(@PathVariable Integer id) {
        serviceOfArticle.deleteArticle(id);
        return new ResponseEntity<>("Article deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Integer id) {
        Article article = serviceOfArticle.getArticleById(id);
        return new ResponseEntity<>(article, HttpStatus.OK);
    }

    // Add more methods for retrieving multiple articles, searching, etc.
}
