package com.example.learnhub.Controller;

import com.example.learnhub.DTO.BlogDTO;
import com.example.learnhub.Entity.Blog;
import com.example.learnhub.Entity.Category;
import com.example.learnhub.Service.ServiceOfBlog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequestMapping("/blogs")
public class BlogController {

    private final ServiceOfBlog serviceOfBlog;

    @Autowired
    public BlogController(ServiceOfBlog serviceOfBlog) {
        this.serviceOfBlog = serviceOfBlog;
    }

    @GetMapping
    public ResponseEntity<List<BlogDTO>> getAllBlogs() {
        List<BlogDTO> blogs = serviceOfBlog.getAllBlogs()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(blogs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogDTO> getBlogById(@PathVariable Integer id) {
        return serviceOfBlog.getBlogById(id)
                .map(blog -> new ResponseEntity<>(convertToDTO(blog), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<BlogDTO> createBlog(@RequestPart BlogDTO blogDTO, @RequestPart MultipartFile image) throws IOException {
        Blog createdBlog = serviceOfBlog.createBlog(blogDTO,image);
        return new ResponseEntity<>(convertToDTO(createdBlog), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogDTO> updateBlog(@PathVariable Integer id, @RequestBody BlogDTO updatedBlogDTO) {
        Blog updatedBlog = convertToEntity(updatedBlogDTO);
        Blog blog = serviceOfBlog.updateBlog(id, updatedBlog);
        if (blog != null) {
            return new ResponseEntity<>(convertToDTO(blog), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlog(@PathVariable Integer id) {
        serviceOfBlog.deleteBlog(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/sort/{category}")
    public ResponseEntity<List<BlogDTO>> getCoursesByCategory(@PathVariable String category) {
        List<BlogDTO> sortedBlogs = serviceOfBlog.getCoursesByCategory(category)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(sortedBlogs, HttpStatus.OK);
    }

    @GetMapping("/sort/old")
    public ResponseEntity<List<BlogDTO>> getCoursesByDateOld() {
        List<BlogDTO> sortedBlogs = serviceOfBlog.getCoursesByDateOld()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(sortedBlogs, HttpStatus.OK);
    }

    @GetMapping("/sort/new")
    public ResponseEntity<List<BlogDTO>> getCoursesByDateNew() {
        List<BlogDTO> sortedBlogs = serviceOfBlog.getCoursesByDateNew()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(sortedBlogs, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<BlogDTO>> findCoursesByKeyword(@RequestParam("keyword") String keyword) {
        List<BlogDTO> foundBlogs = serviceOfBlog.findCoursesByKeyword(keyword)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(foundBlogs, HttpStatus.OK);
    }


        private BlogDTO convertToDTO(Blog blog) {
            BlogDTO blogDTO = new BlogDTO();
            blogDTO.setId(blog.getId());
            blogDTO.setTitle(blog.getTitle());
            blogDTO.setDescription(blog.getDescription());
            blogDTO.setCreatedDate(blog.getCreatedDate());
            blogDTO.setCategoryName(blog.getCategory().getCategoryName());
            blogDTO.setImageUrl(blog.getImageUrl());
            return blogDTO;
        }

        private Blog convertToEntity(BlogDTO blogDTO) {
            Blog blog = new Blog();
            blog.setId(blogDTO.getId());
            blog.setTitle(blogDTO.getTitle());
            blog.setDescription(blogDTO.getDescription());
            blog.setCategory(blogDTO.getCategory());
            blog.setCreatedDate(blogDTO.getCreatedDate());
            blog.setImageUrl(blogDTO.getImageUrl());
            return blog;
        }


}