package com.example.learnhub.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "CartItem")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CartItemID")
    private Integer cartItemId;

    @ManyToOne
    @JoinColumn(name = "CourseID")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "CartID")
    private Cart cart;

}
