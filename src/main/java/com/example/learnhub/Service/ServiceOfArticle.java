package com.example.learnhub.Service;

import com.example.learnhub.Entity.Article;
import com.example.learnhub.Entity.Section;
import com.example.learnhub.Repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceOfArticle implements IServiceOfArticle {

    private final ServiceOfFile serviceOfFile;
    private final ArticleRepository articleRepository;

    @Autowired
    public ServiceOfArticle(ServiceOfFile serviceOfFile, ArticleRepository articleRepository) {
        this.serviceOfFile = serviceOfFile;
        this.articleRepository = articleRepository;
    }

    @Override
    public Article createArticle(Article article) {
        return articleRepository.save(article);
    }

    @Override
    public Optional<Article> findArticleById(Integer id) {
        return articleRepository.findById(id);
    }



    @Override
    public void deleteArticle(Integer id) {
        articleRepository.deleteById(id);
    }

    @Override
    public void createArticles(Section section, List<MultipartFile> articleFiles) {
        if (articleFiles == null || articleFiles.isEmpty()) {
            throw new IllegalArgumentException("No Article files provided");
        }

        for (MultipartFile articleFile : articleFiles) {
            if (articleFile.isEmpty()) {
                // Skip empty files
                continue;
            }

            try {

                if (isArticleFile(articleFile)) {
                    // Upload article file to GCS using ServiceOfFile
                    serviceOfFile.uploadFile(articleFile);

                    // Create Article entity
                    Article article = new Article();
                    article.setSectionID(section.getSectionId()); // Set the section

                    // Save the Article entity
                    articleRepository.save(article);
                } else {
                    throw new IllegalArgumentException("File is not a valid article file");
                }
            } catch (IOException e) {
                // Handle upload failure
                e.printStackTrace();
            }
        }
    }



    private boolean isArticleFile(MultipartFile file) {
        String filename = file.getOriginalFilename();
        return StringUtils.hasText(filename) && getArticleFileExtensions().stream()
                .anyMatch(ext -> filename.toLowerCase().endsWith(ext));
    }

    private List<String> getArticleFileExtensions() {
        // Add more video file extensions as needed (e.g., mkv, flv, etc.)
        return List.of(".pdf", ".txt", ".doc", ".docx");
    }
    // Other methods for handling articles
}
