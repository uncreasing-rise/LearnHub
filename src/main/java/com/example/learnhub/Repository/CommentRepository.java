package com.example.learnhub.Repository;

import com.example.learnhub.Entity.Comment;
import com.example.learnhub.Entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    // Save a comment
    Comment save(Comment comment);

    // Find a comment by its ID
    Optional<Comment> findById(Integer id);

    // Find all comments
    List<Comment> findAll();

    // Delete a comment
    void delete(Comment comment);


    @Query("SELECT c FROM Comment c WHERE c.course.courseId = :courseId")
    List<Comment> findByCourse(@Param("courseId") Integer courseId);
}
