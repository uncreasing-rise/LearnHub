package com.example.learnhub.Controller;

import com.example.learnhub.DTO.CommentDTO;
import com.example.learnhub.Entity.Comment;
import com.example.learnhub.Service.ServiceOfComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final ServiceOfComment serviceOfComment;

    @Autowired
    public CommentController(ServiceOfComment serviceOfComment) {
        this.serviceOfComment = serviceOfComment;
    }

    @PostMapping("/create")
    public ResponseEntity<Comment> createComment(@RequestBody CommentDTO commentDTO) {
        Comment createdComment = serviceOfComment.createComment(commentDTO);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Integer id, @RequestBody CommentDTO updatedCommentDTO) {
        Comment updatedComment = serviceOfComment.updateComment(id, updatedCommentDTO);
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
