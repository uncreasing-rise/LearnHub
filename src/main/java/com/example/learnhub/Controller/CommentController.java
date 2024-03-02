package com.example.learnhub.Controller;

import com.example.learnhub.DTO.CommentDTO;
import com.example.learnhub.Entity.Comment;
import com.example.learnhub.Service.ServiceOfComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
