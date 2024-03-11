package com.example.learnhub.Service;

import com.example.learnhub.DTO.ArticleDTO;
import com.example.learnhub.Entity.Article;
import com.example.learnhub.Entity.Section;
import com.example.learnhub.Exceptions.SectionNotFoundException;
import com.example.learnhub.Repository.ArticleRepository;
import com.example.learnhub.Repository.SectionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceOfArticle implements IServiceOfArticle {

    private final ServiceOfFile serviceOfFile;
    private final ArticleRepository articleRepository;
    private final SectionRepository sectionRepository;


    @Autowired
    public ServiceOfArticle(ServiceOfFile serviceOfFile, ArticleRepository articleRepository, SectionRepository sectionRepository) {
        this.serviceOfFile = serviceOfFile;
        this.articleRepository = articleRepository;
        this.sectionRepository = sectionRepository;
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

    public List<Article> createArticles(Section section, List<MultipartFile> articleFiles, List<ArticleDTO> articles) {
        List<Article> createdArticles = new ArrayList<>();

        if (articleFiles == null || articleFiles.isEmpty() || articles == null || articles.isEmpty() || articleFiles.size() != articles.size()) {
            throw new IllegalArgumentException("Invalid article files or DTOs provided");
        }
        try {
            for (int i = 0; i < articleFiles.size(); i++) {
                MultipartFile articleFile = articleFiles.get(i);
                ArticleDTO articleDTO = articles.get(i); // Get the corresponding ArticleDTO
                if (isArticleFile(articleFile)) {
                    // Upload article file to GCS using ServiceOfFile
                    String articleFilePath = serviceOfFile.uploadFile(articleFile);
                    // Create Article entity
                    Article article = new Article();
                    article.setSection(section); // Set the section ID
                    article.setArticleUrl(constructFileUrl(articleFilePath)); // Set the file path
                    article.setTitle(articleDTO.getTitle()); // Set title from the DTO
                    // Save the Article entity
                    createdArticles.add(articleRepository.save(article));
                } else {
                    throw new IllegalArgumentException("File is not a valid article file");
                }
            }
        } catch (IOException e) {
            // Handle upload failure
            e.printStackTrace();
        }
        return createdArticles;
    }

    public String constructFileUrl(String originalFilename) {
        // Trả về URL công khai cho file
        return "https://storage.googleapis.com/" + "learnhub_bucket" + "/" + originalFilename;
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

    @Transactional
    public Article updateArticle(int articleId, MultipartFile articleFile, ArticleDTO articleDTO) {
        // Check if the file and DTO are valid
        if (articleFile == null || articleDTO == null) {
            throw new IllegalArgumentException("Invalid article file or DTO provided");
        }

        // Check if the file is a valid article file
        if (!isArticleFile(articleFile)) {
            throw new IllegalArgumentException("File is not a valid article file");
        }

        try {
            // Upload the article file to GCS and get the file path
            String articleFilePath = serviceOfFile.uploadFile(articleFile);

            // Retrieve the article by its ID
            Optional<Article> optionalArticle = articleRepository.findById(articleId);
            if (optionalArticle.isPresent()) {
                Article existingArticle = optionalArticle.get();
                // Update article properties
                existingArticle.setSection(articleDTO.getSection()); // Set the section ID from DTO
                existingArticle.setArticleUrl(constructFileUrl(articleFilePath)); // Set the file path
                existingArticle.setTitle(articleDTO.getTitle()); // Set title from the DTO
                // Save the updated article
                articleRepository.save(existingArticle);
            } else {
                throw new IllegalArgumentException("Article not found for ID: " + articleId);
            }
        } catch (IOException e) {
            // Handle upload failure
            e.printStackTrace();
            throw new RuntimeException("Failed to update article: " + e.getMessage());
        }
        return null;
    }

    public Article createArticleToSection(Integer sectionId, MultipartFile articleFile, ArticleDTO articleDTO) {
        // Kiểm tra xem phần (section) có tồn tại không
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new SectionNotFoundException(sectionId));

        // Kiểm tra xem tệp được cung cấp có phải là tệp bài báo hợp lệ không
        if (!isArticleFile(articleFile)) {
            throw new IllegalArgumentException("File is not a valid article file");
        }
        try {
            // Tải tệp bài báo lên GCS bằng ServiceOfFile và lấy đường dẫn tệp
            String articleFilePath = serviceOfFile.uploadFile(articleFile);
            // Tạo entity Article và thiết lập các thuộc tính
            Article article = new Article();
            article.setSection(section); // Thiết lập phần (section)
            article.setArticleUrl(constructFileUrl(articleFilePath)); // Thiết lập đường dẫn tệp
            article.setTitle(articleDTO.getTitle()); // Thiết lập tiêu đề từ DTO
            // Lưu entity Article và trả về nó
            return articleRepository.save(article);
        } catch (IOException e) {
            // Xử lý lỗi khi tải tệp lên
            e.printStackTrace();
            throw new RuntimeException("Failed to upload article file: " + e.getMessage());
        }
    }

    @Transactional
    public boolean deleteArticleFromSection(Integer sectionId, Integer articleId) {
        try {
            Optional<Section> optionalSection = sectionRepository.findById(sectionId);
            if (optionalSection.isPresent()) {
                Section section = optionalSection.get();
                Optional<Article> optionalArticle = articleRepository.findById(articleId);
                if (optionalArticle.isPresent()) {
                    Article article = optionalArticle.get();
                    articleRepository.delete(article);
                    return true; // Successfully deleted article
                } else {
                    throw new IllegalArgumentException("Article not found for ID: " + articleId);
                }
            } else {
                throw new IllegalArgumentException("Section not found for ID: " + sectionId);
            }
        } catch (Exception e) {
            // Handle delete failure
            e.printStackTrace();
            return false;
        }
    }

    // Other methods for handling articles
}
