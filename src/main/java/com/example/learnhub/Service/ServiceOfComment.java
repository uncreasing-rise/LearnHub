package com.example.learnhub.Service;

import com.example.learnhub.DTO.CommentDTO;
import com.example.learnhub.Entity.Comment;
import com.example.learnhub.Repository.CommentRepository;
import com.example.learnhub.Repository.CourseRepository;
import com.example.learnhub.Repository.UserRepository;
import com.example.learnhub.Repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
