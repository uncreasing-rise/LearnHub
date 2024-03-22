package com.example.learnhub.Repository;

import com.example.learnhub.Entity.Blog;
import com.example.learnhub.Entity.Category;
import com.example.learnhub.Entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {

    @Query("SELECT c FROM Blog c WHERE c.category.categoryId = :categoryId")
    List<Blog> findByCategory(@Param("categoryId") String categoryId);

    List<Blog> findAllByOrderByCreatedDateDesc();

    List<Blog> findAllByOrderByCreatedDateAsc();

    @Query("SELECT c FROM Blog c WHERE c.title LIKE %:keyword% OR c.description LIKE %:keyword%")
    List<Blog> findByKeyword(@Param("keyword") String keyword);
}
