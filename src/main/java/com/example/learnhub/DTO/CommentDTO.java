package com.example.learnhub.DTO;

import lombok.Data;

@Data
public class CommentDTO {
    private Integer commentId;
    private String commentText;
    private Integer userId;
    private Integer courseId;
    private Integer videoId;

}
