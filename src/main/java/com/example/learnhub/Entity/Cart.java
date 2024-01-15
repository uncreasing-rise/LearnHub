package com.example.learnhub.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "Cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CartID")
    private Integer cartId;

    @Column(name = "Total")
    private Double total;

    @ManyToOne
    @JoinColumn(name = "UserID")  // Assuming "accountId" is the foreign key column in the Cart table
    private User user;  // Change to the correct attribute name for the relationship

    @Column(name = "CartDate")
    private Date cartDate;

}
