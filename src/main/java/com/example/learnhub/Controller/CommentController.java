package com.example.learnhub.Controller;

import com.example.learnhub.DTO.CommentDTO;
import com.example.learnhub.Entity.Comment;
import com.example.learnhub.Service.ServiceOfComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@CrossOrigin("*")

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final ServiceOfComment serviceOfComment;

    @Autowired
    public CommentController(ServiceOfComment serviceOfComment) {
        this.serviceOfComment = serviceOfComment;
    }

    @PostMapping("/create")
    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentDTO commentDTO) {
        Comment createdComment = serviceOfComment.leaveCommentAtCourse(commentDTO.getUserId(), commentDTO.getCourseId(), commentDTO.getCommentText());
        CommentDTO createdCommentDTO = mapToDTO(createdComment);
        return new ResponseEntity<>(createdCommentDTO, HttpStatus.CREATED);
    }
    private CommentDTO mapToDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setUserId(comment.getUser().getUserId());
        commentDTO.setCourseId(comment.getCourse().getCourseId());
        commentDTO.setCommentText(comment.getCommentText());
        commentDTO.setCreatedDate(comment.getCreatedDate());
        // Map other properties as needed
        return commentDTO;
    }
    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Integer id, @RequestBody CommentDTO updatedCommentDTO) {
        Comment updatedComment = serviceOfComment.updateComment(id, updatedCommentDTO.getCommentText());
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Integer id) {
        serviceOfComment.deleteComment(id);
        return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Integer id) {
        Optional<Comment> optionalComment = serviceOfComment.getCommentById(id);
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            return ResponseEntity.ok(comment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // Add more methods for retrieving multiple comments, searching, etc.
}
