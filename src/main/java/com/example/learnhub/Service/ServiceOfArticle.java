package com.example.learnhub.Service;

import com.example.learnhub.DTO.ArticleDTO;
import com.example.learnhub.Entity.Article;
import com.example.learnhub.Entity.Section;
import com.example.learnhub.Repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceOfArticle implements IServiceOfArticle {

    private final ArticleRepository articleRepository;

    @Autowired
    public ServiceOfArticle(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Article createArticle(Article article) {
        return articleRepository.save(article);
    }

    public void createArticles(Section section, List<ArticleDTO> articleDTOs) {
        if (articleDTOs != null) {
            for (ArticleDTO articleDTO : articleDTOs) {
                Article article = new Article();
//                article.setTitle(articleDTO.getTitle());
                article.setArticleData(articleDTO.getArticleData());
                article.setSection(article.getSection());
                createArticle(article);
            }
        }
    }
}
