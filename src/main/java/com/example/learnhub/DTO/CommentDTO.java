package com.example.learnhub.DTO;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentDTO {
    private Integer commentId;
    private String commentText;
    private String userName;
    private Integer courseId;
    private String userImage;
    private LocalDateTime createdDate;
private Integer userId;}
