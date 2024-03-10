package com.example.learnhub.Service;

import com.example.learnhub.DTO.ArticleDTO;
import com.example.learnhub.Entity.Article;
import com.example.learnhub.Entity.Section;
import com.example.learnhub.Exceptions.ArticleNotFoundException;
import com.example.learnhub.Repository.ArticleRepository;
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

    public List<Article> createArticlesForSection(Section section, List<MultipartFile> articleFiles, List<ArticleDTO> articles) {
        List<Article> createdArticles = new ArrayList<>();

        try {
            // Kiểm tra tính hợp lệ của danh sách file bài viết và DTO bài viết
            if (articleFiles == null || articleFiles.isEmpty() || articles == null || articles.isEmpty() || articleFiles.size() != articles.size()) {
                throw new IllegalArgumentException("Invalid article files or DTOs provided");
            }

            // Lặp qua từng file bài viết và DTO tương ứng
            for (int i = 0; i < articleFiles.size(); i++) {
                MultipartFile articleFile = articleFiles.get(i);
                ArticleDTO articleDTO = articles.get(i);

                // Kiểm tra xem file có phải là file bài viết hợp lệ không
                if (isArticleFile(articleFile)) {
                    // Upload file bài viết lên GCS và lấy đường dẫn file
                    String articleFilePath = serviceOfFile.uploadFile(articleFile);

                    // Tạo entity Article mới
                    Article article = new Article();
                    article.setSection(section); // Set the section ID
                    article.setArticleUrl(constructFileUrl(articleFilePath)); // Set the file path
                    article.setTitle(articleDTO.getTitle()); // Set title from the DTO

                    // Lưu lại bài viết đã tạo
                    createdArticles.add(articleRepository.save(article));
                } else {
                    throw new IllegalArgumentException("File is not a valid article file");
                }
            }
        } catch (IOException e) {
            // Xử lý khi upload file thất bại
            e.printStackTrace();
        }

        return createdArticles;
    }
    public List<Article> updateArticlesForSection(List<ArticleDTO> updatedArticlesDTO) {
        List<Article> updatedArticles = new ArrayList<>();

        // Lặp qua từng DTO bài viết cần chỉnh sửa
        for (ArticleDTO updatedArticleDTO : updatedArticlesDTO) {
            // Kiểm tra xem DTO có chứa ID của bài viết không
            if (updatedArticleDTO.getArticleId() != null) {
                // Nếu có ID, ta cập nhật bài viết đã tồn tại
                Article article = articleRepository.findById(updatedArticleDTO.getArticleId())
                        .orElseThrow(() -> new ArticleNotFoundException(updatedArticleDTO.getArticleId()));

                // Cập nhật thông tin cho bài viết
                article.setTitle(updatedArticleDTO.getTitle());
                // Cập nhật các thuộc tính khác của article nếu cần

                // Lưu lại bài viết đã cập nhật
                updatedArticles.add(articleRepository.save(article));
            } else {
                throw new IllegalArgumentException("Article ID is required for updating");
            }
        }

        return updatedArticles;
    }


    // Other methods for handling articles
}
