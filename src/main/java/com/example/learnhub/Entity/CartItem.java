package com.example.learnhub.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "CartItem")
public class CartItem {
    @Id
    @Column(name = "CartItemID")
    private Integer cartItemId;

    @Column(name = "CourseID")
    private Integer courseId;

    @Column(name = "CartID")
    private Integer cartId;


}
