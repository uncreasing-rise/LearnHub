package com.example.learnhub.Service;

import com.example.learnhub.DTO.BlogDTO;
import com.example.learnhub.Entity.Blog;
import com.example.learnhub.Entity.Category;
import com.example.learnhub.Entity.Course;
import com.example.learnhub.Repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceOfBlog {
    private final BlogRepository blogRepository;
    private final ServiceOfFile serviceOfFile;

    @Autowired
    public ServiceOfBlog(BlogRepository blogRepository, ServiceOfFile serviceOfFile) {
        this.blogRepository = blogRepository;
        this.serviceOfFile = serviceOfFile;
    }


    public List<Blog> getCoursesByDateOld() {
        return blogRepository.findAllByOrderByCreatedDateAsc();
    }

    public List<Blog> getCoursesByDateNew() {
        return blogRepository.findAllByOrderByCreatedDateDesc();
    }
    public List<Blog> getCoursesByCategory(String category) {
        return blogRepository.findByCategory(category);
    }

    public List<Blog> findCoursesByKeyword(String keyword) {
        return blogRepository.findByKeyword(keyword);
    }
    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }

    public Optional<Blog> getBlogById(Integer id) {
        return blogRepository.findById(id);
    }

    public Blog createBlog(BlogDTO blogDTO, MultipartFile image) throws IOException {
        // Check if title and description are not empty
        if (blogDTO.getTitle() == null || blogDTO.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (blogDTO.getDescription() == null || blogDTO.getDescription().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }

        Blog blog = new Blog();
        blog.setCreatedDate(LocalDateTime.now());
        blog.setTitle(blogDTO.getTitle());
        blog.setDescription(blogDTO.getDescription());
        blog.setCategory(blogDTO.getCategory());
        blog.setImageUrl(serviceOfFile.uploadImage(image));
        return blogRepository.save(blog);
    }

    public Blog updateBlog(Integer id, Blog updatedBlogDTO) {
        Optional<Blog> optionalBlog = blogRepository.findById(id);
        if (optionalBlog.isPresent()) {
            Blog blog = optionalBlog.get();
            blog.setTitle(updatedBlogDTO.getTitle());
            blog.setDescription(updatedBlogDTO.getDescription());
            blog.setCategory(updatedBlogDTO.getCategory());
            blog.setCreatedDate(updatedBlogDTO.getCreatedDate());

            return blogRepository.save(blog);
        } else {
            throw new IllegalArgumentException("Blog with id " + id + " not found");
        }
    }

    public void deleteBlog(Integer id) {
        blogRepository.deleteById(id);
    }
}
