package com.example.learnhub.Service;

import com.example.learnhub.DTO.CommentDTO;
import com.example.learnhub.Entity.Comment;
import com.example.learnhub.Repository.CommentRepository;
import com.example.learnhub.Repository.CourseRepository;
import com.example.learnhub.Repository.UserRepository;
import com.example.learnhub.Repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServiceOfComment {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final VideoRepository videoRepository;

    @Autowired
    public ServiceOfComment(CommentRepository commentRepository, UserRepository userRepository,
                            CourseRepository courseRepository, VideoRepository videoRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.videoRepository = videoRepository;
    }

    public Comment createComment(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setCommentText(commentDTO.getCommentText());
        comment.setUser(userRepository.findById(commentDTO.getUserId()).orElse(null));
        comment.setCourse(courseRepository.findById(commentDTO.getCourseId()).orElse(null));
        comment.setVideo(videoRepository.findById(commentDTO.getVideoId()).orElse(null));
        return commentRepository.save(comment);
    }

    public Optional<Comment> getCommentById(Integer commentId) {
        return commentRepository.findById(commentId);
    }

    public Comment updateComment(Integer commentId, CommentDTO updatedCommentDTO) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            comment.setCommentText(updatedCommentDTO.getCommentText());
            comment.setUser(userRepository.findById(updatedCommentDTO.getUserId()).orElse(null));
            comment.setCourse(courseRepository.findById(updatedCommentDTO.getCourseId()).orElse(null));
            comment.setVideo(videoRepository.findById(updatedCommentDTO.getVideoId()).orElse(null));
            return commentRepository.save(comment);
        } else {
            throw new IllegalArgumentException("Comment not found with id: " + commentId);
        }
    }

    public void deleteComment(Integer commentId) {
        commentRepository.deleteById(commentId);
    }
}
