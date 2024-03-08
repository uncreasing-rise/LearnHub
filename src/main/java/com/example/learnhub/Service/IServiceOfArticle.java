package com.example.learnhub.Service;

import com.example.learnhub.DTO.ArticleDTO;
import com.example.learnhub.Entity.Article;
import com.example.learnhub.Entity.Section;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface IServiceOfArticle {

    Article createArticle(Article article);

    Optional<Article> findArticleById(Integer id);

    void deleteArticle(Integer id);

}
