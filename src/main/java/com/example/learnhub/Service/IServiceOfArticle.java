package com.example.learnhub.Service;

import com.example.learnhub.DTO.ArticleDTO;
import com.example.learnhub.Entity.Article;
import com.example.learnhub.Entity.Section;

import java.util.List;

public interface IServiceOfArticle {
    Article createArticle(Article article);

    void createArticles(Section section, List<ArticleDTO> articleDTOs);
}
