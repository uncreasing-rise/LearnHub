package com.example.learnhub.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentDTO {
    private Integer commentId;
    private String commentText;
    private Integer userId;
    private Integer courseId;

}
