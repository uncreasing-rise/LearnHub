package com.example.learnhub.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Wishlist")
public class Wishlist {
    @Id
    @Column(name = "WishlistID")
    private Integer wishlistId;

    @Column(name = "UserID")
    private Integer userId;

    @Column(name = "CourseID")
    private Integer courseId;

}
