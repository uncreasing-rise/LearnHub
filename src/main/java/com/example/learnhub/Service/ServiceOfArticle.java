package com.example.learnhub.Service;

import com.example.learnhub.DTO.ArticleDTO;
import com.example.learnhub.Entity.Article;
import com.example.learnhub.Entity.Section;
import com.example.learnhub.Service.IServiceOfArticle;
import com.example.learnhub.Service.ServiceOfFile;
import com.example.learnhub.Repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Service
public class ServiceOfArticle implements IServiceOfArticle {

    private final ArticleRepository articleRepository;
    private final ServiceOfFile serviceOfFile;

    @Autowired
    public ServiceOfArticle(ArticleRepository articleRepository, ServiceOfFile serviceOfFile) {
        this.articleRepository = articleRepository;
        this.serviceOfFile = serviceOfFile;
    }

    public Article createArticle(Article article) {
        return articleRepository.save(article);
    }

    public void createArticles(Section section, List<ArticleDTO> articleDTOs) {
        if (articleDTOs != null) {
            for (ArticleDTO articleDTO : articleDTOs) {
                try {
                    // Check if the file is a text file
                    if (isTextFile(articleDTO.getArticleFile())) {
                        // Upload article file to GCS using ServiceOfFile
                        serviceOfFile.uploadFile(articleDTO.getArticleFile());

                        // Create Article entity and associate it with the Section
                        Article article = new Article();
                        // Set other properties of the article as needed
                        article.setArticleData(""); // Set article data if needed
                        article.setSection(section); // Set the section

                        // Save the Article entity
                        createArticle(article);
                    } else {
                        // Handle non-text files
                        System.out.println("File " + articleDTO.getArticleFile().getOriginalFilename() + " is not a text file.");
                    }
                } catch (IOException e) {
                    // Handle upload failure
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean isTextFile(MultipartFile file) {
        // Get the file extension
        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());

        // List of supported text file extensions
        List<String> supportedExtensions = List.of("txt", "pdf", "doc", "docx");

        // Check if the file extension is in the supported list
        return supportedExtensions.contains(extension.toLowerCase());
    }
}
