package com.example.learnhub.Repository;

import com.example.learnhub.Entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
    // Add any custom query methods if needed
}
