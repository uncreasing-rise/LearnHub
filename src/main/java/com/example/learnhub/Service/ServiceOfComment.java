package com.example.learnhub.Service;

import com.example.learnhub.DTO.CommentDTO;
import com.example.learnhub.Entity.Comment;
import com.example.learnhub.Entity.Course;
import com.example.learnhub.Entity.User;
import com.example.learnhub.Repository.CommentRepository;
import com.example.learnhub.Repository.CourseRepository;
import com.example.learnhub.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ServiceOfComment {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public ServiceOfComment(CommentRepository commentRepository, UserRepository userRepository,
                            CourseRepository courseRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    // Method to leave a comment at a course
    public Comment leaveCommentAtCourse(Integer userId, Integer courseId, String commentText) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setCourse(course);
        comment.setCommentText(commentText);
        comment.setCreatedDate(LocalDateTime.now());

        commentRepository.save(comment);
        return comment;
    }

    // Method to update a comment
    public Comment updateComment(Integer commentId, String updatedCommentText) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        comment.setCommentText(updatedCommentText);
        commentRepository.save(comment);
        return comment;
    }

    // Method to retrieve a comment by ID
    public Optional<Comment> getCommentById(Integer commentId) {
        return commentRepository.findById(commentId);
    }

    // Method to delete a comment
    public void deleteComment(Integer commentId) {
        commentRepository.deleteById(commentId);
    }

    public List<Comment> findByCourseID(Integer courseId) {
        return commentRepository.findByCourse(courseId) ;

    }
}
