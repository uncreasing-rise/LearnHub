package com.example.learnhub.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "Cart")
public class Cart {
    @Id
    @Column(name = "CartID")
    private Integer cartId;

    @Column(name = "Total")
    private Double total;

    @Column(name = "UserID")
    private Integer userId;

    @Column(name = "CartDate")
    private Date cartDate;


}
