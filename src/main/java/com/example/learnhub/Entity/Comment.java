package com.example.learnhub.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;


@Data
@Entity
@Table(name = "Comment")
public class Comment {
    @Id
    @Column(name = "CommentID")
    private Integer commentId;

    @Column(name = "CommentText")
    private String commentText;

    @Column(name = "UserID")
    private Integer userId;

    @Column(name = "CourseID")
    private Integer courseId;

    @Column(name = "VideoID")
    private Integer videoId;

}
